package com.tasks.user_management.remote.controllers;

import com.tasks.user_management.remote.requests.SendUserInstance;
import com.tasks.user_management.services.RefreshTokenService;
import com.tasks.user_management.utils.exceptions.TokenValidationException;
import com.tasks.user_management.utils.payload.SendUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class TokenController {

    private static final Logger log = LoggerFactory.getLogger(TokenController.class);
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
    public ResponseEntity<SendUserDto> validateToken(@RequestHeader("Authorization") String token) throws TokenValidationException, IOException, InterruptedException {
        SendUserDto user = refreshTokenService.validateToken(token);
        try {
            SendUserInstance.sendInstance(new SendUserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRoles()));
        } catch (Exception e) {
            log.error("Error sending user instance: {}", e.getMessage());
        }
        log.info("Token validated successfully");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
