package com.tasks.user_management.services;

import com.tasks.user_management.models.User;
import com.tasks.user_management.utils.exceptions.AuthenticationFailedException;
import com.tasks.user_management.utils.exceptions.UserAlreadyExistsException;
import com.tasks.user_management.utils.exceptions.UserNotFound;
import com.tasks.user_management.utils.payload.LoginDto;
import com.tasks.user_management.utils.payload.UserDto;

public interface UserService {
    void createUser(UserDto user) throws UserAlreadyExistsException;
    LoginDto authenticateUser(String email, String password) throws AuthenticationFailedException;
    void deleteUser(String email) throws UserNotFound;
    UserDto updateUser(UserDto userDto) throws UserNotFound;
    User getUserByEmail(String email);
}
