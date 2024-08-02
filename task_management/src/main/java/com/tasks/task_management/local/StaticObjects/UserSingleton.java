package com.tasks.task_management.local.StaticObjects;


import com.tasks.task_management.remote.utils.payload.UserInstance;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class UserSingleton {
    @Getter
    private static UserSingleton instance;
    private String username;
    private String email;
    private List<String> roles;

    private UserSingleton() {
    }

    public static synchronized UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }

    public static void setInstance(UserInstance userInstance) {
        instance = new UserSingleton();
        instance.setEmail(userInstance.email());
        instance.setUsername(userInstance.username());
        instance.setRoles(userInstance.roles());
    }
}
