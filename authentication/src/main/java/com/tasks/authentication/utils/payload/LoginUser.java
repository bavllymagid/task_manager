package com.tasks.authentication.utils.payload;

import lombok.Data;

@Data
public class LoginUser {
    private String email;
    private String password;
}
