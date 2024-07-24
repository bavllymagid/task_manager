package com.tasks.user_management.services;

import com.tasks.user_management.models.RefreshToken;
import com.tasks.user_management.models.User;
import com.tasks.user_management.models.UserRole;
import com.tasks.user_management.repositories.RefreshTokenRepository;
import com.tasks.user_management.repositories.UserRepository;
import com.tasks.user_management.repositories.UserRolesRepository;
import com.tasks.user_management.utils.RolesConst;
import com.tasks.user_management.utils.exceptions.AuthenticationFailedException;
import com.tasks.user_management.utils.exceptions.TokenValidationException;
import com.tasks.user_management.utils.exceptions.UserAlreadyExistsException;
import com.tasks.user_management.utils.exceptions.UserNotFound;
import com.tasks.user_management.utils.jwt.JwtUtil;
import com.tasks.user_management.utils.payload.LoginDto;
import com.tasks.user_management.utils.payload.UserDto;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    UserRepository userRepository;
    RefreshTokenRepository refreshTokenRepository;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;
    RefreshTokenService refreshTokenService;
    UserRolesRepository userRolesRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RefreshTokenRepository refreshTokenRepository,
                           RefreshTokenService refreshTokenService,
                           UserRolesRepository userRolesRepository,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenService = refreshTokenService;
        this.userRolesRepository = userRolesRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void createUser(UserDto user) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists.");
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        newUser.setSecretToken("");
        newUser.setUserRoles(setFirstRole(newUser));
        userRepository.save(newUser);
        userRolesRepository.saveAll(newUser.getUserRoles());
    }

    private List<UserRole> setFirstRole(User user) {
        List<UserRole> userRoles = new ArrayList<>();
        userRoles.add(new UserRole(user, RolesConst.USER.name()));
        return userRoles;
    }

    @Override
    public LoginDto authenticateUser(String email, String password) throws AuthenticationFailedException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new AuthenticationFailedException("Invalid email or password.");
        }
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.get());
        userRepository.updateSecretTokenByEmail(email, RandomStringUtils.randomAlphanumeric(12));
        UserDto userDto = new UserDto(user.get().getUsername(), user.get().getEmail(),"");
        String token = jwtUtil.generateToken(email, new Date(System.currentTimeMillis()+ 43200000), userRepository.findSecretTokenByEmail(email));
        return new LoginDto(token,
                refreshToken.getRefreshToken(),
                userDto);
    }

    @Override
    @Transactional
    public void deleteUser(String email, String token) throws TokenValidationException, UserNotFound {
        if(!refreshTokenService.validateToken(token, email)) throw new TokenValidationException("Invalid token");

        userRepository.deleteByEmail(email);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, String token) throws TokenValidationException, UserNotFound {
        if(!refreshTokenService.validateToken(token, userDto.getEmail())) throw new TokenValidationException("Invalid token");

        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isEmpty()) {
            throw new UserNotFound("User with email " + userDto.getEmail() + " not found.");
        }
        user.get().setUsername(userDto.getUsername());
        user.get().setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user.get());
        return userDto;
    }
}
