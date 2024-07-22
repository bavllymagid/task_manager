package com.tasks.authentication.controllers;

import com.tasks.authentication.services.RefreshTokenService;
import com.tasks.authentication.utils.exceptions.TokenValidationException;
import com.tasks.authentication.utils.payload.TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    private final RefreshTokenService refreshTokenService;

    @Autowired
    public TokenController(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/api/token/refresh")
    public ResponseEntity<String> refreshToken(@RequestBody TokenDto refreshToken) throws TokenValidationException {
        return new ResponseEntity<>(refreshTokenService.refreshAccessToken(refreshToken.getToken()), HttpStatus.OK);
    }

    @PostMapping("/api/token/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody TokenDto token) throws TokenValidationException {
        return new ResponseEntity<>(refreshTokenService.validateToken(token.getToken()), HttpStatus.OK);
    }

}
