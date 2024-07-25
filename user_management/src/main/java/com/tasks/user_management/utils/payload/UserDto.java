package com.tasks.user_management.utils.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private BigInteger user_id;
    private String username;
    private String email;
    private String password;
}
