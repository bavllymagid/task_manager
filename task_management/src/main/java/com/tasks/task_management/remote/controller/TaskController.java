package com.tasks.task_management.remote.controller;

import com.tasks.task_management.UserSingelton;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    @PostMapping("/task/recieve_instance")
    public void recieveInstance(@RequestBody UserSingelton userSingelton) {
        System.out.println("Recieved instance: " + userSingelton.toString());
    }
}
