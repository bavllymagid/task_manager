package com.tasks.task_management.remote.kafka.utils;

import com.tasks.task_management.utils.payload.UserInstance;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PendingRequestManager {

    private final ConcurrentHashMap<String, CompletableFuture<UserInstance>> pendingRequests = new ConcurrentHashMap<>();

    public void createRequest(String correlationId) {
        CompletableFuture<UserInstance> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);
    }

    public CompletableFuture<UserInstance> getRequest(String correlationId) {
        return pendingRequests.get(correlationId);
    }

    public CompletableFuture<UserInstance> removeRequest(String correlationId) {
        return pendingRequests.remove(correlationId);
    }
}

