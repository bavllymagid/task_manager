package com.tasks.authentication.controllers;

import com.tasks.authentication.models.User;
import com.tasks.authentication.repositories.UserRepository;
import com.tasks.authentication.services.UserService;
import com.tasks.authentication.utils.Roles;
import com.tasks.authentication.utils.exceptions.AuthenticationFailedException;
import com.tasks.authentication.utils.exceptions.UserAlreadyExistsException;
import com.tasks.authentication.utils.payload.LoginUser;
import com.tasks.authentication.utils.payload.RegisteredUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/users/register")
    public ResponseEntity<RegisteredUser> createUser(@RequestBody RegisteredUser user) throws UserAlreadyExistsException {
        userService.createUser(user, Roles.USER.name());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginUser user) throws AuthenticationFailedException {
        return ResponseEntity.ok(userService.authenticateUser(user.getEmail(), user.getPassword()).get("token").toString());
    }
}
