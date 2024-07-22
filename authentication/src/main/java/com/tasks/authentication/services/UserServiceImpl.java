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
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    UserRepository userRepository;
    RefreshTokenRepository refreshTokenRepository;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RefreshTokenRepository refreshTokenRepository,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
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
        String refreshToken = storeRefreshToken(user.get());
        UserDto userDto = new UserDto(user.get().getUsername(), user.get().getEmail(), user.get().getPassword());
        return new LoginDto(jwtUtil.generateToken(email), refreshToken, userDto);
    }

    private String storeRefreshToken(User user){
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId()).orElse(new RefreshToken());
        refreshToken.setUser(user);
        refreshToken.setRefreshToken(jwtUtil.generateRefreshToken(user.getEmail()));
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(30));
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getRefreshToken();
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
        refreshTokenRepository.delete(refreshTokenRepository.findByUserId(user.get().getId()).get());
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
