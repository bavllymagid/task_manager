package com.tasks.user_management.services;

import com.tasks.user_management.models.RefreshToken;
import com.tasks.user_management.models.User;
import com.tasks.user_management.utils.exceptions.TokenValidationException;

public interface RefreshTokenService {
    String refreshAccessToken(String refreshToken) throws TokenValidationException;
    boolean validateToken(String token) throws TokenValidationException;
    RefreshToken createRefreshToken(User user);
    String getSecretFromEmail(String token);
}
