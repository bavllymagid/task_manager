package com.tasks.user_management.services;

import com.tasks.user_management.utils.exceptions.AuthenticationFailedException;
import com.tasks.user_management.utils.exceptions.TokenValidationException;
import com.tasks.user_management.utils.exceptions.UserAlreadyExistsException;
import com.tasks.user_management.utils.exceptions.UserNotFoundException;
import com.tasks.user_management.utils.payload.LoginDto;
import com.tasks.user_management.utils.payload.UserDto;

import java.util.List;

public interface UserService {
    void createUser(UserDto user) throws UserAlreadyExistsException;
    LoginDto authenticateUser(String email, String password) throws AuthenticationFailedException;
    void deleteUser(String email, String token) throws TokenValidationException, UserNotFoundException;
    UserDto updateUser(UserDto userDto, String token) throws TokenValidationException, UserNotFoundException;
    List<UserDto> getListOfUsers(String token) throws TokenValidationException;
    UserDto getUser(String email, String token) throws TokenValidationException, UserNotFoundException;
    void logoutUser(String token, String email) throws TokenValidationException;
    UserDto addRoleToUser(String email, String role, String token) throws TokenValidationException, UserNotFoundException;
}
