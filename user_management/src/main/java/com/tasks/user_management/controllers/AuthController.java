package com.tasks.user_management.controllers;

import com.tasks.user_management.services.UserService;
import com.tasks.user_management.utils.exceptions.AuthenticationFailedException;
import com.tasks.user_management.utils.exceptions.UserAlreadyExistsException;
import com.tasks.user_management.utils.exceptions.UserNotFound;
import com.tasks.user_management.utils.payload.LoginDto;
import com.tasks.user_management.utils.payload.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<LoginDto> authenticateUser(@RequestBody UserDto user) throws AuthenticationFailedException {
        return ResponseEntity.ok(userService.authenticateUser(user.getEmail(), user.getPassword()));
    }

    @PutMapping("/api/users/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto user) throws UserNotFound {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/api/users/delete")
    public ResponseEntity<String> deleteUser(@RequestBody UserDto user) throws UserNotFound {
        userService.deleteUser(user.getEmail());
        return ResponseEntity.ok("User deleted successfully.");
    }

}
