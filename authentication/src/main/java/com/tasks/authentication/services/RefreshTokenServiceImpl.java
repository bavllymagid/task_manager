package com.tasks.authentication.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tasks.authentication.models.RefreshToken;
import com.tasks.authentication.models.User;
import com.tasks.authentication.repositories.RefreshTokenRepository;
import com.tasks.authentication.repositories.UserRepository;
import com.tasks.authentication.utils.exceptions.TokenValidationException;
import com.tasks.authentication.utils.jwt.JwtUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{
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
            token.setSecretToken(RandomStringUtils.randomAlphanumeric(12));
            refreshTokenRepository.save(token);
            return jwtUtil.generateToken(email, new Date(System.currentTimeMillis() + 43200000), token.getSecretToken());
        } else {
            throw new TokenValidationException("Invalid refresh token");
        }
    }

    @Override
    public boolean validateToken(String token) throws TokenValidationException {
        token = token.replace("Bearer ", "");
        if (jwtUtil.validateToken(token, getSecretFromEmail(token)) != null) {
            return true;
        } else {
            throw new TokenValidationException("Invalid token");
        }
    }

    public RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = refreshTokenRepository.findByUserEmail(user.getEmail()).orElse(new RefreshToken());
        refreshToken.setUser(user);
        refreshToken.setSecretToken(RandomStringUtils.randomAlphanumeric(12));
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
    public String getSecretFromEmail(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return refreshTokenRepository.findByUserEmail(jwt.getSubject()).get().getSecretToken();
    }

}
