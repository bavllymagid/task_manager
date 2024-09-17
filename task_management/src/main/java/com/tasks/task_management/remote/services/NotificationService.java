package com.tasks.task_management.remote.services;

import com.tasks.task_management.local.models.Notification;
import com.tasks.task_management.local.models.Task;

import java.math.BigInteger;
import java.util.List;

public interface NotificationService {
    void sendNotification(String message, String userId);
    void generateDueDateNotification();
    void generateOnActivityNotification(List<Notification> notifications);
}
