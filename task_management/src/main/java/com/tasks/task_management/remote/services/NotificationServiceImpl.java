package com.tasks.task_management.remote.services;

import com.tasks.task_management.local.StaticObjects.NotificationType;
import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.local.models.Notification;
import com.tasks.task_management.local.models.Task;
import com.tasks.task_management.local.repositories.NotificationRepository;
import com.tasks.task_management.local.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    NotificationRepository notificationRepository;
    TaskRepository taskRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   TaskRepository taskRepository) {
        this.notificationRepository = notificationRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void sendNotification(String message, String userId) {


    }

    @Override
    @Scheduled(fixedRate = 3600000)
    public void generateDueDateNotification() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDay = now.plusDays(1);

        List<Task> tasks = taskRepository.findTasksDueWithinADayByUserId(now, nextDay, UserSingleton.getInstance().getId());

        for (Task task : tasks) {
            Notification notification = new Notification();
            notification.setMessage("Task " + task.getTitle() + " is due in less than a day!");
            notification.setTaskId(task.getTaskId());
            notification.setUserId(task.getUserId());
            notification.setType(NotificationType.DUE_DATE.name());
            notificationRepository.save(notification);
        }
    }

    @Override
    public void generateOnActivityNotification(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }
}
