package com.tasks.authentication.utils.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {
    String token;
    String refreshToken;
    UserDto user;
}
