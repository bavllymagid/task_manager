package com.tasks.authentication.services;

import com.tasks.authentication.models.RefreshToken;
import com.tasks.authentication.models.User;
import com.tasks.authentication.repositories.RefreshTokenRepository;
import com.tasks.authentication.repositories.UserRepository;
import com.tasks.authentication.utils.exceptions.AuthenticationFailedException;
import com.tasks.authentication.utils.exceptions.UserAlreadyExistsException;
import com.tasks.authentication.utils.exceptions.UserNotFound;
import com.tasks.authentication.utils.jwt.JwtUtil;
import com.tasks.authentication.utils.payload.LoginDto;
import com.tasks.authentication.utils.payload.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    UserRepository userRepository;
    RefreshTokenRepository refreshTokenRepository;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;
    RefreshTokenService refreshTokenService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RefreshTokenRepository refreshTokenRepository,
                           RefreshTokenService refreshTokenService,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void createUser(UserDto user, String role) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists.");
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(role);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(newUser);
    }

    @Override
    public LoginDto authenticateUser(String email, String password) throws AuthenticationFailedException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new AuthenticationFailedException("Invalid email or password.");
        }
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.get());
        UserDto userDto = new UserDto(user.get().getUsername(), user.get().getEmail(), user.get().getPassword());
        return new LoginDto(jwtUtil.generateToken(email,
                new Date(System.currentTimeMillis()+ 43200000), refreshToken.getSecretToken()),
                refreshToken.getRefreshToken(),
                userDto);
    }

    @Override
    public void chooseRole(UserDto userDto, String role) throws UserAlreadyExistsException {
        createUser(userDto, role);
    }

    @Override
    public void deleteUser(String email) throws UserNotFound {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFound("User with email " + email + " not found.");
        }
        userRepository.delete(user.get());
        refreshTokenRepository.delete(refreshTokenRepository.findByUserEmail(user.get().getEmail()).get());
    }

    @Override
    public UserDto updateUser(UserDto userDto) throws UserNotFound {
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
