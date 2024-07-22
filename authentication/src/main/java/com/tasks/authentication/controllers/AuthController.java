package com.tasks.authentication.controllers;

import com.tasks.authentication.services.UserService;
import com.tasks.authentication.utils.Roles;
import com.tasks.authentication.utils.exceptions.AuthenticationFailedException;
import com.tasks.authentication.utils.exceptions.UserAlreadyExistsException;
import com.tasks.authentication.utils.payload.LoginDto;
import com.tasks.authentication.utils.payload.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/users/register")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) throws UserAlreadyExistsException {
        userService.chooseRole(user, Roles.USER.name());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<LoginDto> authenticateUser(@RequestBody UserDto user) throws AuthenticationFailedException {
        return ResponseEntity.ok(userService.authenticateUser(user.getEmail(), user.getPassword()));
    }

    @PostMapping("/api/admin/register")
    public ResponseEntity<UserDto> createAdmin(@RequestBody UserDto user) throws UserAlreadyExistsException {
        userService.chooseRole(user, Roles.ADMIN.name());
        return ResponseEntity.ok(user);
    }


}
