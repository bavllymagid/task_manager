package com.tasks.task_management.remote.controller;

import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.remote.utils.payload.UserInstance;
import com.tasks.task_management.remote.utils.requests.Requests;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    @PostMapping("/task/recieve_instance")
    public void recieveInstance(@RequestBody UserInstance user) {
        UserSingleton.setInstance(user);
        System.out.println("Recieved instance: " + user.toString());
    }

    @GetMapping("/task/validate")
    public boolean validateUser() {
        boolean b = Requests.validateToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlRW1haWxAZXhhbXBsZTEuY29tIiwiZXhwIjoxNzIyNjY5OTcwLCJpYXQiOjE3MjI2MjY3NzB9.YzOven6vV70dHoxYX54CTiw7BVqY1Pp9s5bXIVXMehA");
        System.out.println("Validated user: " + b);
        return b;
    }
}
