package com.tasks.task_management.remote.services;

import com.tasks.task_management.local.exceptions.TaskNotFoundException;
import com.tasks.task_management.local.models.Task;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.util.List;

public interface AssignTaskService {
    void assignTaskToAll(List<BigInteger> userIds, BigInteger taskId);
    void unassignTaskFromAll(List<BigInteger> userIds, BigInteger taskId);
    void unassignAllTasks(BigInteger userId);
    Page<Task> getUserAssignedTasks(BigInteger userId, int page, int size);
}
