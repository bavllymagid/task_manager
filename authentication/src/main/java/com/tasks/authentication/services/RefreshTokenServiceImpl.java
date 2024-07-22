package com.tasks.authentication.services;

import com.tasks.authentication.utils.exceptions.TokenValidationException;
import com.tasks.authentication.utils.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{
    JwtUtil jwtUtil;

    @Autowired
    public RefreshTokenServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Override
    public String refreshAccessToken(String refreshToken) throws TokenValidationException {
        String email = jwtUtil.validateToken(refreshToken);
        if (email != null) {
            return jwtUtil.generateToken(email);
        } else {
            throw new TokenValidationException("Invalid refresh token");
        }
    }

    @Override
    public boolean validateToken(String token) throws TokenValidationException {
        if(jwtUtil.validateToken(token) != null) {
            return true;
        } else {
            throw new TokenValidationException("Invalid token");
        }
    }
}
