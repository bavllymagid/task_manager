package com.tasks.user_management.services;

import com.auth0.jwt.JWT;
import com.tasks.user_management.models.RefreshToken;
import com.tasks.user_management.models.User;
import com.tasks.user_management.repositories.RefreshTokenRepository;
import com.tasks.user_management.repositories.UserRepository;
import com.tasks.user_management.utils.exceptions.TokenValidationException;
import com.tasks.user_management.utils.exceptions.UserNotFound;
import com.tasks.user_management.utils.jwt.JwtUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

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
        String email = jwtUtil.validateToken(refreshToken, token.getSecretRefresh());
        if (email != null) {
            String newToken = saveAccessToken(email);
            refreshTokenRepository.save(token);
            return jwtUtil.generateToken(email, new Date(System.currentTimeMillis() + 43200000), newToken);
        } else {
            throw new TokenValidationException("Invalid refresh token");
        }
    }

    private String saveAccessToken(String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFound("User not found"));
            user.setSecretToken(RandomStringUtils.randomAlphanumeric(12));
            userRepository.save(user);
            return user.getSecretToken();
        } catch (UserNotFound e) {
            log.error(e.getMessage());
            return "";
        }
    }

    @Override
    public boolean validateToken(String token) throws TokenValidationException {
        token = token.replace("Bearer ", "");
        if (jwtUtil.validateToken(token, userRepository.findSecretTokenByEmail(JWT.decode(token).getSubject())) != null) {
            return true;
        } else {
            throw new TokenValidationException("Invalid token");
        }
    }

    public RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = refreshTokenRepository.findByUserEmail(user.getEmail()).orElse(new RefreshToken());
        refreshToken.setUser(user);
        refreshToken.setSecretRefresh(RandomStringUtils.randomAlphanumeric(12));
        refreshToken.setRefreshToken(jwtUtil.generateToken(user.getEmail(),
                new Date(System.currentTimeMillis() + 2592000000L),
                refreshToken.getSecretRefresh()));
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(30));
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public User getUserFromToken(String token){
        return userRepository.findByEmail(JWT.decode(token).getSubject()).get();
    }

}
