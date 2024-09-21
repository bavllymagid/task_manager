package com.tasks.task_management.remote.controller;

import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.remote.services.NotificationService;
import com.tasks.task_management.remote.services.TaskService;
import com.tasks.task_management.remote.utils.payload.UserInstance;
import com.tasks.task_management.remote.utils.requests.Requests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/task")
public class UserController {

    private final TaskService taskService;
    private final NotificationService notificationService;

    @Autowired
    public UserController(TaskService taskService,
                          NotificationService notificationService) {
        this.taskService = taskService;
        this.notificationService = notificationService;
    }

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

    @DeleteMapping("/delete_user/{userId}")
    public void deleteUser(@RequestHeader("Authorization") String token,
                           @PathVariable BigInteger userId ) {
        taskService.deleteAllTasks(userId);
        notificationService.deleteAllNotificationsByUserId(userId);
    }

    @GetMapping("/Invalidate")
    public void invalidateUser(@RequestHeader("Authorization") String token) {
        UserSingleton.invalidateInstance();
    }


}
