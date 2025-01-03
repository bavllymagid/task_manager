package com.tasks.task_management.kafka.consumer;

import com.tasks.task_management.kafka.payload.AuthenticatedUserPayload;
import com.tasks.task_management.kafka.utils.PendingRequestManager;
import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.remote.utils.payload.UserInstance;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Component
public class KafkaListeners {
    private final PendingRequestManager pendingRequests;

    @Autowired
    public KafkaListeners(PendingRequestManager pendingRequests) {
        this.pendingRequests = pendingRequests;
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
}
