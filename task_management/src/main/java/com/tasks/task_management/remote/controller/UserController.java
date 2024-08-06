package com.tasks.task_management.remote.controller;

import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.remote.utils.payload.UserInstance;
import com.tasks.task_management.remote.utils.requests.Requests;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
public class UserController {

    @PostMapping("/receive_instance")
    public void receive_instance(@RequestBody UserInstance user) {
        UserSingleton.setInstance(user);
        System.out.println("Recieved instance: " + user.toString());
    }

    @GetMapping("/validate")
    public boolean validateUser(@RequestHeader("Authorization") String token) {
        boolean b = Requests.validateToken(token);
        System.out.println("Validated user: " + b);
        return b;
    }
}
