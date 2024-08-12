package com.tasks.user_management.services;

import com.tasks.user_management.utils.exceptions.AuthenticationFailedException;
import com.tasks.user_management.utils.exceptions.TokenValidationException;
import com.tasks.user_management.utils.exceptions.UserAlreadyExistsException;
import com.tasks.user_management.utils.exceptions.UserNotFound;
import com.tasks.user_management.utils.payload.LoginDto;
import com.tasks.user_management.utils.payload.UserDto;

import java.util.List;

public interface UserService {
    void createUser(UserDto user) throws UserAlreadyExistsException;
    LoginDto authenticateUser(String email, String password) throws AuthenticationFailedException;
    void deleteUser(String email, String token) throws TokenValidationException, UserNotFound ;
    UserDto updateUser(UserDto userDto, String token) throws TokenValidationException, UserNotFound ;
    List<UserDto> getListOfUsers(String token) throws TokenValidationException;
}
