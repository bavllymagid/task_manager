package com.tasks.user_management.services;

import com.auth0.jwt.JWT;
import com.tasks.user_management.local.models.RefreshToken;
import com.tasks.user_management.local.models.User;
import com.tasks.user_management.local.repositories.RefreshTokenRepository;
import com.tasks.user_management.local.repositories.UserRepository;
import com.tasks.user_management.local.repositories.UserRolesRepository;
import com.tasks.user_management.remote.requests.InvalidateUser;
import com.tasks.user_management.utils.RolesConst;
import com.tasks.user_management.utils.exceptions.AuthenticationFailedException;
import com.tasks.user_management.utils.exceptions.TokenValidationException;
import com.tasks.user_management.utils.exceptions.UserAlreadyExistsException;
import com.tasks.user_management.utils.exceptions.UserNotFoundException;
import com.tasks.user_management.utils.jwt.JwtUtil;
import com.tasks.user_management.utils.payload.LoginDto;
import com.tasks.user_management.utils.payload.UserDto;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService{

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
        newUser.setUserRoles(new ArrayList<>(List.of(userRolesRepository.findByName(RolesConst.USER.name()))));
        userRepository.save(newUser);
    }

    @Override
    public LoginDto authenticateUser(String email, String password) throws AuthenticationFailedException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new AuthenticationFailedException("Invalid email or password.");
        }
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.get());
        userRepository.updateSecretTokenByEmail(email, RandomStringUtils.randomAlphanumeric(12));
        String token = jwtUtil.generateToken(user.get(), new Date(System.currentTimeMillis()+ 43200000), userRepository.findSecretTokenByEmail(email));
        return new LoginDto(token, refreshToken.getRefreshToken(), user.get());
    }

    @Override
    @Transactional
    public void deleteUser(String email, String token) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " not found.");
        }
        if(InvalidateUser.deleteUser(token, user.get().getId()))
            userRepository.deleteByEmail(email);
        else throw new UserNotFoundException("User with email " + email + " not found.");
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, String token) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with email " + userDto.getEmail() + " not found.");
        }
        user.get().setUsername(userDto.getUsername());
        user.get().setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user.get());
        return userDto;
    }

    @Override
    public Page<UserDto> getListOfUsers(String token, int size, int page){
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<User> users = userRepository.findAllExceptCurrentUser(JWT.decode(token.substring(7)).getSubject(), pageable);
        return users.map(user -> new UserDto(user.getId(), user.getUsername(), user.getEmail(), ""));
    }

    @Override
    public UserDto getUser(String email, String token) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " not found.");
        }
        return new UserDto(user.get().getId(), user.get().getUsername(), user.get().getEmail(), "");
    }

    @Override
    public void logoutUser(String token) throws TokenValidationException {
        if(InvalidateUser.invalidateUser(token)) {
            token = token.substring(7);
            String email = JWT.decode(token).getSubject();
            userRepository.updateSecretTokenByEmail(email, "");
            refreshTokenRepository.deleteByUserEmail(email);
        }
        else throw new TokenValidationException("Invalid token.");
    }

    @Override
    public UserDto addRoleToUser(String email, String role, String token) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " not found.");
        }
        if(role.equalsIgnoreCase("user")){
            user.get().setUserRoles(new ArrayList<>(List.of(userRolesRepository.findByName(getRole(role)))));
        }else {
            user.get().getUserRoles().add(userRolesRepository.findByName(getRole(role)));
        }
        userRepository.save(user.get());
        return new UserDto(user.get().getId(), user.get().getUsername(), user.get().getEmail(), "");
    }

    private String getRole(String role){
        return switch (role.toUpperCase()) {
            case "ADMIN" -> RolesConst.ADMIN.name();
            case "MANAGER" -> RolesConst.MANAGER.name();
            default -> RolesConst.USER.name();
        };
    }
}
