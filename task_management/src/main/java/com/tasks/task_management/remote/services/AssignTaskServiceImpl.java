package com.tasks.task_management.remote.services;

import com.tasks.task_management.local.StaticObjects.NotificationType;
import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.local.exceptions.TaskNotFoundException;
import com.tasks.task_management.local.models.Notification;
import com.tasks.task_management.local.models.Task;
import com.tasks.task_management.local.models.TaskAssignment;
import com.tasks.task_management.local.repositories.TaskAssRepository;
import com.tasks.task_management.local.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssignTaskServiceImpl implements AssignTaskService {

    TaskAssRepository assignmentRepository;
    TaskRepository taskRepository;
    NotificationService notificationService;

    @Autowired
    public AssignTaskServiceImpl(TaskAssRepository assignmentRepository,
                                 TaskRepository taskRepository,
                                 NotificationService notificationService) {
        this.assignmentRepository = assignmentRepository;
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void assignTaskToAll(List<BigInteger> userIds, BigInteger taskId) throws TaskNotFoundException{
        List<TaskAssignment> taskAssignments = new ArrayList<>();
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        for (BigInteger userId : userIds) {
            if(!assignmentRepository.existsByTask_TaskIdAndUserId(taskId, userId)) {
                if(task.getUserId().equals(UserSingleton.getInstance().getId())){
                    TaskAssignment taskAssignment = new TaskAssignment();
                    taskAssignment.setTask(task);
                    taskAssignment.setUserId(userId);
                    taskAssignment.setAssignedBy(UserSingleton.getInstance().getId());
                    taskAssignments.add(taskAssignment);
                }
                else
                    throw new TaskNotFoundException("Task not found or already assigned");
            }
            else
                throw new TaskNotFoundException("Task not found or already assigned");
        }
        assignmentRepository.saveAll(taskAssignments);

        addNotification("Task assigned to you", NotificationType.ASSIGNED.name(), userIds, task);
    }

    @Override
    public void unassignTaskFromAll(List<BigInteger> userIds, BigInteger taskId) throws TaskNotFoundException {
        for (BigInteger userId : userIds) {
            if(assignmentRepository.existsByTask_TaskIdAndUserIdAndAssignedBy(taskId,
                    userId, UserSingleton.getInstance().getId())) {
                assignmentRepository.deleteAllByTask_TaskIdAndUserIdAndAssignedBy(taskId,
                        userId,
                        UserSingleton.getInstance().getId());
                addNotification("Task unassigned from you", NotificationType.UNASSIGNED.name(), List.of(userId), taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found")));
            }
            else
                throw new TaskNotFoundException("Task not found");
        }
    }

    @Override
    public void unassignAllTasks(BigInteger userId) throws TaskNotFoundException {
        if(!assignmentRepository.existsByUserId(userId))
            throw new TaskNotFoundException("Task not found");
        assignmentRepository.deleteByUserIdAndAssignedBy(userId, UserSingleton.getInstance().getId());

        List<Task> tasks = assignmentRepository.getAllByUserId(userId);
        addNotification("Task unassigned from you", NotificationType.UNASSIGNED.name(), List.of(userId), tasks.toArray(new Task[0]));
    }

    @Override
    public Page<Task> getUserAssignedTasks(BigInteger userId, int page, int size) {
        Pageable pageable = PageRequest.of(0, 20);
        return assignmentRepository.findAllByUserId(userId,pageable);
    }

    void addNotification(String message, String type, List<BigInteger> userId, Task... task) {
        List<Notification> notifications = new ArrayList<>();
        for (Task t : task){
            for (BigInteger id : userId) {
                Notification notification = new Notification();
                notification.setMessage(message);
                notification.setTaskId(t.getTaskId());
                notification.setUserId(id);
                notification.setType(type);
                notification.setRead(false);
                notifications.add(notification);
            }
        }
        notificationService.generateOnActivityNotification(notifications);
    }
}
