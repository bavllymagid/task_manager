package com.tasks.task_management.remote.services;

import com.tasks.task_management.local.exceptions.TaskNotFoundException;
import com.tasks.task_management.local.models.Task;
import com.tasks.task_management.remote.dto.TaskDto;
import org.springframework.data.domain.Page;
import java.math.BigInteger;

public interface TaskService {
    void createTask(TaskDto task);
    void updateTask(Task task) throws TaskNotFoundException;
    void deleteTask(BigInteger taskId) throws TaskNotFoundException;
    Task getTask(BigInteger taskId) throws TaskNotFoundException;
    Page<Task> getUserCreatedTasks(BigInteger id, int page, int size);
    void assignTask(BigInteger taskID, BigInteger userID);
}
