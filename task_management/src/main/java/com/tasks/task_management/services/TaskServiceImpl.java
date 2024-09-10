package com.tasks.task_management.services;

import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.local.exceptions.TaskNotFoundException;
import com.tasks.task_management.local.models.Task;
import com.tasks.task_management.local.models.TaskAssignment;
import com.tasks.task_management.local.repositories.TaskAssRepository;
import com.tasks.task_management.local.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskServiceImpl implements TaskService {

    TaskRepository taskRepository;
    TaskAssRepository assignmentRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskAssRepository assignmentRepository) {
        this.taskRepository = taskRepository;
        this.assignmentRepository = assignmentRepository;
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
    public List<Task> getUserCreatedTasks(BigInteger id) {
        return taskRepository.findAllByUserId(id).orElseGet(ArrayList::new);
    }

    @Override
    public void assignTask(BigInteger taskID, BigInteger userID) {
        TaskAssignment assignment = new TaskAssignment();

        assignment.setTaskId(taskID);
        assignment.setAssignedBy(UserSingleton.getInstance().getId());
        assignment.setUserId(userID);
        assignment.setAssignedAt(LocalDateTime.now());

        assignmentRepository.save(assignment);
    }

    @Override
    public List<Task> getAssignedTasks(BigInteger userID){
        Optional<List<Task>> tasks = taskRepository.getTasksByUserId(userID);
        return tasks.orElseGet(ArrayList::new);
    }

}
