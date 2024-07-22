package com.tasks.authentication.services;

import com.tasks.authentication.utils.exceptions.TokenValidationException;
import com.tasks.authentication.utils.payload.UserDto;

public interface RefreshTokenService {
    String refreshAccessToken(String refreshToken) throws TokenValidationException;
}
