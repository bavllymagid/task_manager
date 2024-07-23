package com.tasks.user_management.utils.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {
    String token;
    String refreshToken;
    UserDto user;
}
