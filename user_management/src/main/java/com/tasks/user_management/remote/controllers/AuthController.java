package com.tasks.user_management.remote.controllers;

import com.tasks.user_management.local.models.UserRole;
import com.tasks.user_management.remote.requests.SendUserInstance;
import com.tasks.user_management.services.UserService;
import com.tasks.user_management.utils.exceptions.AuthenticationFailedException;
import com.tasks.user_management.utils.exceptions.TokenValidationException;
import com.tasks.user_management.utils.exceptions.UserAlreadyExistsException;
import com.tasks.user_management.utils.exceptions.UserNotFoundException;
import com.tasks.user_management.utils.payload.LoginDto;
import com.tasks.user_management.utils.payload.RoleChangeDto;
import com.tasks.user_management.utils.payload.SendUserDto;
import com.tasks.user_management.utils.payload.UserDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) throws UserAlreadyExistsException {
        userService.createUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDto> authenticateUser(@RequestBody UserDto user) throws AuthenticationFailedException, IOException, InterruptedException {
        LoginDto loginDto = userService.authenticateUser(user.getEmail(), user.getPassword());
        List<String> roles = loginDto.getUser().getUserRoles().stream().map(UserRole::getName).toList();
        try {
            SendUserInstance.sendInstance(new SendUserDto(loginDto.getUser().getId(), loginDto.getUser().getUsername(), loginDto.getUser().getEmail(), roles));
        } catch (Exception e) {
            log.error("Error sending user instance: {}", e.getMessage());
        }
        return ResponseEntity.ok(loginDto);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestHeader("Authorization") String token,
                                              @RequestBody UserDto user) throws UserNotFoundException, TokenValidationException {
        return ResponseEntity.ok(userService.updateUser(user, token));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String token,
                                             @Email @RequestParam("email") String email) throws UserNotFoundException, TokenValidationException {
        userService.deleteUser(email, token);
        return ResponseEntity.ok("User deleted successfully.");
    }

    @GetMapping("/get_users")
    public ResponseEntity<List<UserDto>> getUsers(@RequestHeader("Authorization") String token) throws TokenValidationException {
        return ResponseEntity.ok(userService.getListOfUsers(token));
    }

    @GetMapping("/get_user")
    public ResponseEntity<UserDto> getUser(@RequestHeader("Authorization") String token,
                                          @RequestParam("email")
                                          @Email
                                          String email) throws TokenValidationException, UserNotFoundException {
        return ResponseEntity.ok(userService.getUser(email, token));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String token) throws TokenValidationException {
        userService.logoutUser(token);
        return ResponseEntity.ok("User logged out successfully.");
    }

    @PutMapping("/add_role")
    public ResponseEntity<UserDto> addRoleToUser(@RequestHeader("Authorization") String token,
                                                 @Valid @RequestBody RoleChangeDto role) throws TokenValidationException, UserNotFoundException {
        return ResponseEntity.ok(userService.addRoleToUser(role.getEmail(), role.getRole(), token));
    }

}
