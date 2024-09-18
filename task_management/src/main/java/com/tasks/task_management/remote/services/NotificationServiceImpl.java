package com.tasks.task_management.remote.services;

import com.tasks.task_management.local.StaticObjects.NotificationType;
import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.local.models.Notification;
import com.tasks.task_management.local.models.Task;
import com.tasks.task_management.local.repositories.NotificationRepository;
import com.tasks.task_management.local.repositories.TaskAssRepository;
import com.tasks.task_management.local.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    NotificationRepository notificationRepository;
    TaskRepository taskRepository;
    TaskAssRepository taskAssRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   TaskRepository taskRepository,
                                   TaskAssRepository taskAssRepository) {
        this.notificationRepository = notificationRepository;
        this.taskRepository = taskRepository;
        this.taskAssRepository = taskAssRepository;
    }

    @Override
    public Page<Notification> sendNotification(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return notificationRepository.findAllByUserId(UserSingleton.getInstance().getId(), pageable);
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public void generateDueDateNotification() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDay = now.plusDays(1);

        List<Task> tasks = taskRepository.findTasksDueWithinADayByUserId(now, nextDay);
        List<Notification> notifications = new ArrayList<>();
        for (Task task : tasks) {
            List<BigInteger> userIds = taskAssRepository.findAllUserIdByTask_TaskId(task.getTaskId());
            for(BigInteger userId : userIds){
                if(notificationRepository.existsByUserIdAndTaskId(userId, task.getTaskId()))
                    continue;
                Notification notification = new Notification();
                notification.setMessage("Task " + task.getTitle() + " is due in less than a day!");
                notification.setTaskId(task.getTaskId());
                notification.setUserId(userId);
                notification.setType(NotificationType.DUE_DATE.name());
                notification.setRead(false);
                notifications.add(notification);
            }
        }
        notificationRepository.saveAll(notifications);
    }

    @Override
    public void generateOnActivityNotification(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }

    @Override
    @Scheduled(fixedRate = 259200000) // 259200000 milliseconds = 3 days
    public void deleteNotificationsEvery3Days() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysAgo = now.minusDays(3);
        notificationRepository.deleteByCreatedAtBefore(threeDaysAgo);
    }

    @Override
    public Notification updateNotificationStatus(BigInteger notificationId) {
        Notification notification = notificationRepository.findById(notificationId).get();
        notification.setRead(true);
        return notificationRepository.save(notification);
    }
}
