package com.tasks.user_management.utils.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
public class SendUserDto {
    private BigInteger id;
    private String username;
    private String email;
    private List<String> roles;
}
