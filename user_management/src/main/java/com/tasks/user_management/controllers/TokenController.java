package com.tasks.user_management.controllers;

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

    @PostMapping("/api/token/refresh")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String refreshToken,
                                               @RequestParam("email") String email) throws TokenValidationException {
        return new ResponseEntity<>(refreshTokenService.refreshAccessToken(refreshToken,email), HttpStatus.OK);
    }

    @PostMapping("/api/token/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token,
                                                 @RequestParam("email") String email) throws TokenValidationException {
        return new ResponseEntity<>(refreshTokenService.validateToken(token, email), HttpStatus.OK);
    }
}
