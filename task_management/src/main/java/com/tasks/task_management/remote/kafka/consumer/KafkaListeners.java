package com.tasks.task_management.remote.kafka.consumer;

import com.tasks.task_management.remote.kafka.payload.AuthenticatedUserPayload;
import com.tasks.task_management.remote.kafka.payload.InvalidateUserPayload;
import com.tasks.task_management.remote.kafka.utils.PendingRequestManager;
import com.tasks.task_management.remote.services.NotificationService;
import com.tasks.task_management.remote.services.TaskService;
import com.tasks.task_management.utils.payload.UserInstance;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

@Component
public class KafkaListeners {
    private final PendingRequestManager pendingRequests;
    private final NotificationService notificationService;
    private final TaskService taskService;

    @Autowired
    public KafkaListeners(PendingRequestManager pendingRequests,
                          NotificationService notificationService,
                          TaskService taskService) {
        this.pendingRequests = pendingRequests;
        this.notificationService = notificationService;
        this.taskService = taskService;
    }

    @KafkaListener(topics = "userAuthenticated", groupId = "group_id")
    public void listenUserAuthenticated(ConsumerRecord<String, String> record) {
        AuthenticatedUserPayload user = AuthenticatedUserPayload.fromJson(record.value());
        String correlationId = String.valueOf(user.correlationId());
        CompletableFuture<UserInstance> future = pendingRequests.removeRequest(correlationId);
        if (future != null) {
            future.complete(user.user());
        }
        System.out.println("Received user authenticated: " + user);
    }

    @KafkaListener(topics = "userDeleted", groupId = "group_id")
    public void listenDeleteUser(ConsumerRecord<String, String> record) {
        InvalidateUserPayload user = InvalidateUserPayload.fromJson(record.value());
        BigInteger userId = BigInteger.valueOf(Long.parseLong(user.id()));
        notificationService.deleteAllNotificationsByUserId(userId);
        taskService.deleteAllTasks(userId);
    }
}
