package com.tasks.user_management.services;

import com.auth0.jwt.JWT;
import com.tasks.user_management.local.models.RefreshToken;
import com.tasks.user_management.local.models.User;
import com.tasks.user_management.local.models.UserRole;
import com.tasks.user_management.local.repositories.RefreshTokenRepository;
import com.tasks.user_management.local.repositories.UserRepository;
import com.tasks.user_management.utils.exceptions.TokenValidationException;
import com.tasks.user_management.utils.exceptions.UserNotFoundException;
import com.tasks.user_management.utils.jwt.JwtUtil;
import com.tasks.user_management.utils.payload.SendUserDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{
    private static final Logger log = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);
    JwtUtil jwtUtil;
    RefreshTokenRepository refreshTokenRepository;
    UserRepository userRepository;

    @Autowired
    public RefreshTokenServiceImpl(JwtUtil jwtUtil,
                                   RefreshTokenRepository refreshTokenRepository,
                                   UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String refreshAccessToken(String refreshToken) throws TokenValidationException {
        refreshToken = refreshToken.replace("Bearer ", "");
        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenValidationException("Invalid refresh token"));
        String vEmail = jwtUtil.validateToken(refreshToken, token.getSecretRefresh());
        if (vEmail != null) {
            String newToken = saveAccessToken(vEmail);
            refreshTokenRepository.save(token);
            User user = userRepository.findByEmail(vEmail).orElseThrow(() -> new TokenValidationException("Invalid refresh token"));
            return jwtUtil.generateToken(user, new Date(System.currentTimeMillis() + 43200000), newToken);
        } else {
            throw new TokenValidationException("Invalid refresh token");
        }
    }

    private String saveAccessToken(String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
            user.setSecretToken(RandomStringUtils.randomAlphanumeric(12));
            userRepository.save(user);
            return user.getSecretToken();
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    @Override
    public SendUserDto validateToken(String token) throws TokenValidationException {
        token = token.replace("Bearer ", "");
        String email = JWT.decode(token).getSubject();
        if (jwtUtil.validateToken(token, userRepository.findSecretTokenByEmail(email)) != null) {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new TokenValidationException("Invalid token"));
            List<String> roles = user.getUserRoles().stream().map(UserRole::getName).toList();
            return new SendUserDto(user.getId(), user.getUsername(), user.getEmail(), roles);
        } else {
            throw new TokenValidationException("Invalid token");
        }
    }

    public RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = refreshTokenRepository.findByUserEmail(user.getEmail()).orElse(new RefreshToken());
        refreshToken.setUser(user);
        refreshToken.setSecretRefresh(RandomStringUtils.randomAlphanumeric(12));
        refreshToken.setRefreshToken(jwtUtil.generateToken(user,
                new Date(System.currentTimeMillis() + 2592000000L),
                refreshToken.getSecretRefresh()));
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(30));
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public User getUserFromToken(String token) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(JWT.decode(token).getSubject());
        if(user.isEmpty()) throw new UserNotFoundException("User Not Found");
        return user.get();
    }

}
