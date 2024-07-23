package com.tasks.authentication.services;

import com.tasks.authentication.models.RefreshToken;
import com.tasks.authentication.models.User;
import com.tasks.authentication.utils.exceptions.TokenValidationException;
import com.tasks.authentication.utils.payload.TokenDto;
import com.tasks.authentication.utils.payload.UserDto;

public interface RefreshTokenService {
    String refreshAccessToken(String refreshToken) throws TokenValidationException;
    boolean validateToken(String token) throws TokenValidationException;
    RefreshToken createRefreshToken(User user);
    String getSecretFromEmail(String token);
}
