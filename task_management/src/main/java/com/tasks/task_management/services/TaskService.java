package com.tasks.task_management.services;

import com.tasks.task_management.local.exceptions.TaskNotFoundException;
import com.tasks.task_management.local.models.Task;

import java.math.BigInteger;
import java.util.List;

public interface TaskService {
    void createTask(Task task);
    void updateTask(Task task) throws TaskNotFoundException;
    void deleteTask(BigInteger taskId) throws TaskNotFoundException;
    Task getTask(BigInteger taskId) throws TaskNotFoundException;
    List<Task> getTasks();
}
