package com.tasks.task_management.remote.services;

import com.tasks.task_management.local.StaticObjects.NotificationType;
import com.tasks.task_management.local.StaticObjects.TaskStatus;
import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.local.exceptions.InvalidTokenException;
import com.tasks.task_management.local.exceptions.PassedDueDateException;
import com.tasks.task_management.local.exceptions.TaskNotFoundException;
import com.tasks.task_management.local.models.Notification;
import com.tasks.task_management.local.models.Task;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    TaskRepository taskRepository;
    TaskAssRepository assignmentRepository;
    NotificationService notificationService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskAssRepository assignmentRepository,
                           NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.assignmentRepository = assignmentRepository;
        this.notificationService = notificationService;
    }


    @Override
    public TaskDto createTask(TaskDto task) throws InvalidTokenException, PassedDueDateException {
        Task newTask = new Task();
        newTask.setUserId(UserSingleton.getInstance().getId());
        newTask.setTitle(task.getTitle());
        newTask.setDescription(task.getDescription());
        if(task.getDueDate().isBefore(LocalDateTime.now())) {
            throw new PassedDueDateException("Due date has passed");
        }
        newTask.setDueDate(task.getDueDate());
        newTask.setStatus(TaskStatus.PENDING.name());
        taskRepository.save(newTask);
        task.setCreatedAt(LocalDateTime.now());
        task.setUserId(newTask.getUserId());
        return task;
    }

    @Override
    public void updateTask(TaskDto task) throws TaskNotFoundException {
        if(taskRepository.existsById(task.getTaskId())) {
            Task updatedTask = taskRepository.findById(task.getTaskId()).get();
            if(!updatedTask.getUserId().equals(UserSingleton.getInstance().getId())) {
                throw new TaskNotFoundException("Task not found");
            }
            updatedTask.setTitle(task.getTitle());
            updatedTask.setDescription(task.getDescription());
            updatedTask.setDueDate(task.getDueDate());
            taskRepository.save(updatedTask);
            addNotification(updatedTask, "Task: " + updatedTask.getTitle() + " updated",
                    NotificationType.UPDATED.name());
        } else {
            throw new TaskNotFoundException("Task not found");
        }
    }

    @Override
    public void updateTaskStatus(BigInteger taskId, String status) throws TaskNotFoundException {
        switch (status.toLowerCase()) {
            case "completed":
                status = TaskStatus.COMPLETED.name();
                break;
            case "pending":
                status = TaskStatus.PENDING.name();
                break;
            default:
                throw new IllegalArgumentException("Invalid status");
        }
        if(taskRepository.existsById(taskId)) {
            Task task = taskRepository.findById(taskId).get();
            if(!task.getUserId().equals(UserSingleton.getInstance().getId())) {
                throw new TaskNotFoundException("Task not found");
            }
            taskRepository.updateStatusByTaskId(status, taskId);
            addNotification(task, "Task: " + task.getTitle() + " status updated to " + status,
                    NotificationType.UPDATED.name());
        } else {
            throw new TaskNotFoundException("Task not found");
        }
    }

    @Override
    public void deleteTask(BigInteger taskId) throws TaskNotFoundException {
        if(taskRepository.existsById(taskId)) {
            Task task = taskRepository.findById(taskId).get();
            if(task.getUserId().equals(UserSingleton.getInstance().getId())) {
                addNotification(task, "Task: " + task.getTitle() + " unassigned from you",
                        NotificationType.UNASSIGNED.name());
                taskRepository.deleteById(taskId);
            }
            else {
                throw new TaskNotFoundException("Task not found");
            }
        } else {
            throw new TaskNotFoundException("Task not found");
        }
    }

    @Override
    public void deleteAllTasks(BigInteger userId){
        if(taskRepository.existsByUserId(userId) &&
                userId.equals(UserSingleton.getInstance().getId())){
            taskRepository.deleteByUserId(userId);
        }
    }

    @Override
    public Task getTask(BigInteger taskId) throws TaskNotFoundException {
        return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    @Override
    public Page<Task> getUserCreatedTasks(BigInteger id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dueDate").ascending());
        return taskRepository.findTasksByUserId(id, pageable);
    }

    void addNotification(Task task, String message, String type) {
        List<BigInteger> userId = assignmentRepository.findAllUserIdByTask_TaskId(task.getTaskId());
        List<Notification> notifications = new ArrayList<>();
        for (BigInteger id : userId) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setTaskId(task.getTaskId());
            notification.setUserId(id);
            notification.setType(type);
            notification.setRead(false);
            notifications.add(notification);
        }
        notificationService.generateOnActivityNotification(notifications);
    }
}
