package com.tasks.user_management.remote.controllers;

import com.tasks.user_management.services.RefreshTokenService;
import com.tasks.user_management.utils.exceptions.TokenValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TokenController {

    private final RefreshTokenService refreshTokenService;

    @Autowired
    public TokenController(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/api/token/refresh")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String refreshToken) throws TokenValidationException {
        return new ResponseEntity<>(refreshTokenService.refreshAccessToken(refreshToken), HttpStatus.OK);
    }

    @GetMapping("/api/token/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) throws TokenValidationException {
        return new ResponseEntity<>(refreshTokenService.validateToken(token), HttpStatus.OK);
    }
}
