package com.tasks.user_management.remote.controllers;

import com.tasks.user_management.local.models.UserRole;
import com.tasks.user_management.remote.requests.SendUserInstance;
import com.tasks.user_management.services.UserService;
import com.tasks.user_management.utils.exceptions.AuthenticationFailedException;
import com.tasks.user_management.utils.exceptions.TokenValidationException;
import com.tasks.user_management.utils.exceptions.UserAlreadyExistsException;
import com.tasks.user_management.utils.exceptions.UserNotFound;
import com.tasks.user_management.utils.payload.LoginDto;
import com.tasks.user_management.utils.payload.SendUserDto;
import com.tasks.user_management.utils.payload.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/users/register")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) throws UserAlreadyExistsException {
        userService.createUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<LoginDto> authenticateUser(@RequestBody UserDto user) throws AuthenticationFailedException, IOException, InterruptedException {
        LoginDto loginDto = userService.authenticateUser(user.getEmail(), user.getPassword());
        List<String> roles = loginDto.getUser().getUserRoles().stream().map(UserRole::getRole).toList();
        SendUserInstance.sendInstance(new SendUserDto(loginDto.getUser().getId(), loginDto.getUser().getUsername(), loginDto.getUser().getEmail(), roles));
        return ResponseEntity.ok(loginDto);
    }

    @PutMapping("/api/users/update")
    public ResponseEntity<UserDto> updateUser(@RequestHeader("Authorization") String token,
                                              @RequestBody UserDto user) throws UserNotFound, TokenValidationException {
        return ResponseEntity.ok(userService.updateUser(user, token));
    }

    @DeleteMapping("/api/users/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String token,
                                             @RequestParam("email") String email) throws UserNotFound, TokenValidationException {
        userService.deleteUser(email, token);
        return ResponseEntity.ok("User deleted successfully.");
    }

}
