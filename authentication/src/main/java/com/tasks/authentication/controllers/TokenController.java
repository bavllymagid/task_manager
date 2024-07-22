package com.tasks.authentication.controllers;

import com.tasks.authentication.services.RefreshTokenService;
import com.tasks.authentication.utils.exceptions.TokenValidationException;
import com.tasks.authentication.utils.payload.TokenDto;
import com.tasks.authentication.utils.payload.UserDto;
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
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String refreshToken) throws TokenValidationException {
        return new ResponseEntity<>(refreshTokenService.refreshAccessToken(refreshToken), HttpStatus.OK);
    }

    @PostMapping("/api/token/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) throws TokenValidationException {
        return new ResponseEntity<>(refreshTokenService.validateToken(token), HttpStatus.OK);
    }

    @PostMapping("/api/token/get_refresh")
    public ResponseEntity<TokenDto> getRefreshToken(@RequestBody UserDto userDto) throws TokenValidationException {
        return new ResponseEntity<>(refreshTokenService.getRefToken(userDto), HttpStatus.OK);
    }

}
