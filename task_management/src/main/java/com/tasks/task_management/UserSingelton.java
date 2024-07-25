package com.tasks.task_management;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
public class UserSingelton {
    private final BigInteger id;
    private final String username;
    private final String email;
    private final List<String> roles;


    @Override
    public String toString() {
        return "UserSingelton{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles.toString() +
                '}';
    }
}
