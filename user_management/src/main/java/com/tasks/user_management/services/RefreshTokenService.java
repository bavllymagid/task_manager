package com.tasks.user_management.services;

import com.tasks.user_management.local.models.RefreshToken;
import com.tasks.user_management.local.models.User;
import com.tasks.user_management.utils.exceptions.TokenValidationException;
import com.tasks.user_management.utils.exceptions.UserNotFound;

public interface RefreshTokenService {
    String refreshAccessToken(String refreshToken) throws TokenValidationException;
    boolean validateToken(String token) throws TokenValidationException;
    RefreshToken createRefreshToken(User user);
    User getUserFromToken(String token) throws UserNotFound;
}
