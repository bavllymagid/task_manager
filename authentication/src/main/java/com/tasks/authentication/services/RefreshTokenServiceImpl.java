package com.tasks.authentication.services;

import com.tasks.authentication.models.RefreshToken;
import com.tasks.authentication.models.User;
import com.tasks.authentication.repositories.RefreshTokenRepository;
import com.tasks.authentication.repositories.UserRepository;
import com.tasks.authentication.utils.exceptions.TokenValidationException;
import com.tasks.authentication.utils.jwt.JwtUtil;
import com.tasks.authentication.utils.payload.TokenDto;
import com.tasks.authentication.utils.payload.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        String email = jwtUtil.validateToken(refreshToken);
        if (email != null) {
            return jwtUtil.generateToken(email);
        } else {
            throw new TokenValidationException("Invalid refresh token");
        }
    }

    @Override
    public boolean validateToken(String token) throws TokenValidationException {
        token = token.replace("Bearer ", "");
        if (jwtUtil.validateToken(token) != null) {
            return true;
        } else {
            throw new TokenValidationException("Invalid token");
        }
    }

}
