package com.tasks.authentication.utils.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisteredUser {
    private String username;
    private String email;
    private String password;
}
