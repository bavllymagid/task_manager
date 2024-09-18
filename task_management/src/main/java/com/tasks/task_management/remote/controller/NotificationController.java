package com.tasks.task_management.remote.controller;

import com.tasks.task_management.local.models.Notification;
import com.tasks.task_management.remote.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/task/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/send")
    public ResponseEntity<Page<Notification>> sendNotification(@RequestHeader("Authorization") String token,
                                                               @RequestParam("page") int page,
                                                               @RequestParam("size") int size) {
        return ResponseEntity.ok(notificationService.sendNotification(page, size));
    }

    @GetMapping("/update_status")
    public ResponseEntity<Notification> updateNotificationStatus(@RequestHeader("Authorization") String token,
                                                                 @RequestParam("notificationId") BigInteger notificationId) {
        return ResponseEntity.ok(notificationService.updateNotificationStatus(notificationId));
    }
}
