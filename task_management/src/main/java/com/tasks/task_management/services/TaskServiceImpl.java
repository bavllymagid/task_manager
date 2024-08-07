package com.tasks.task_management.services;

import com.tasks.task_management.local.exceptions.TaskNotFoundException;
import com.tasks.task_management.local.models.Task;
import com.tasks.task_management.local.repositories.TaskAssRepository;
import com.tasks.task_management.local.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.List;

public class TaskServiceImpl implements TaskService {

    TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskAssRepository taskAssRepository) {
        this.taskRepository = taskRepository;
    }


    @Override
    public void createTask(Task task) {
        taskRepository.save(task);
    }

    @Override
    public void updateTask(Task task) throws TaskNotFoundException {
        if(taskRepository.existsById(task.getTaskId())) {
            taskRepository.save(task);
        } else {
            throw new TaskNotFoundException("Task not found");
        }
    }

    @Override
    public void deleteTask(BigInteger taskId) throws TaskNotFoundException {
        if(taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
        } else {
            throw new TaskNotFoundException("Task not found");
        }
    }

    @Override
    public Task getTask(BigInteger taskId) throws TaskNotFoundException {
        return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    @Override
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }
}
