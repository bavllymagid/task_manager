package com.tasks.authentication.services;

import com.tasks.authentication.utils.exceptions.AuthenticationFailedException;
import com.tasks.authentication.utils.exceptions.TokenValidationException;
import com.tasks.authentication.utils.exceptions.UserAlreadyExistsException;
import com.tasks.authentication.utils.payload.LoginDto;
import com.tasks.authentication.utils.payload.UserDto;

import java.util.Map;

public interface UserService {
    void createUser(UserDto user, String role) throws UserAlreadyExistsException;
    LoginDto authenticateUser(String email, String password) throws AuthenticationFailedException;
    public void chooseRole(UserDto userDto, String role) throws UserAlreadyExistsException;
}
