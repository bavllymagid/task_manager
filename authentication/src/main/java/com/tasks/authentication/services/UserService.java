package com.tasks.authentication.services;

import com.tasks.authentication.models.User;
import com.tasks.authentication.utils.exceptions.AuthenticationFailedException;
import com.tasks.authentication.utils.exceptions.TokenValidationException;
import com.tasks.authentication.utils.exceptions.UserAlreadyExistsException;
import com.tasks.authentication.utils.payload.RegisteredUser;

import java.util.Map;

public interface UserService {
    void createUser(RegisteredUser user, String role) throws UserAlreadyExistsException;
    Map<String, Object> authenticateUser(String email, String password) throws AuthenticationFailedException;
    boolean validateToken(String token) throws TokenValidationException;
}
