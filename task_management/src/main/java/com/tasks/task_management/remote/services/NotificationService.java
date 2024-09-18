package com.tasks.task_management.remote.services;

import com.tasks.task_management.local.models.Notification;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.util.List;

public interface NotificationService {
    Page<Notification> sendNotification(int page, int size);
    void generateDueDateNotification();
    void generateOnActivityNotification(List<Notification> notifications);
    void deleteNotificationsEveryNDays();
    Notification updateNotificationStatus(BigInteger notificationId);
}
