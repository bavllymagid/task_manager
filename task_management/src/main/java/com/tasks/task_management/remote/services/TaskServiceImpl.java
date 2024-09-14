package com.tasks.task_management.remote.services;

import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.local.exceptions.InvalidTokenException;
import com.tasks.task_management.local.exceptions.PassedDueDateException;
import com.tasks.task_management.local.exceptions.TaskNotFoundException;
import com.tasks.task_management.local.models.Task;
import com.tasks.task_management.local.models.TaskAssignment;
import com.tasks.task_management.local.repositories.TaskAssRepository;
import com.tasks.task_management.local.repositories.TaskRepository;
import com.tasks.task_management.remote.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Service
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
    public TaskDto createTask(TaskDto task) throws InvalidTokenException, PassedDueDateException {
        Task newTask = new Task();
        newTask.setUserId(UserSingleton.getInstance().getId());
        newTask.setTitle(task.getTitle());
        newTask.setDescription(task.getDescription());
        newTask.setCreatedAt(LocalDateTime.now());
        if(task.getDueDate().isBefore(LocalDateTime.now())) {
            throw new PassedDueDateException("Due date has passed");
        }
        newTask.setDueDate(task.getDueDate());
        newTask.setStatus(task.getStatus());
        taskRepository.save(newTask);
        task.setCreatedAt(newTask.getCreatedAt());
        task.setUserId(newTask.getUserId());
        return task;
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
    public Page<Task> getUserCreatedTasks(BigInteger id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return taskRepository.findTasksByUserId(id, pageable);
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
    
}
