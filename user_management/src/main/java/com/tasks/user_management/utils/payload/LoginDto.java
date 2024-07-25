package com.tasks.user_management.utils.payload;

import com.tasks.user_management.local.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {
    String token;
    String refreshToken;
    User user;
}
