package com.tasks.task_management.remote.services;

import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.local.exceptions.TaskNotFoundException;
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
import java.util.List;

@Service
public class AssignTaskServiceImpl implements AssignTaskService {

    TaskAssRepository assignmentRepository;
    TaskRepository taskRepository;

    @Autowired
    public AssignTaskServiceImpl(TaskAssRepository assignmentRepository,
                                 TaskRepository taskRepository) {
        this.assignmentRepository = assignmentRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void assignTask(BigInteger taskId, BigInteger userId) throws TaskNotFoundException {
        TaskAssignment taskAssignment = new TaskAssignment();
        taskAssignment.setTask(taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found")));
        taskAssignment.setUserId(userId);
        taskAssignment.setAssignedBy(UserSingleton.getInstance().getId());
        assignmentRepository.save(taskAssignment);
    }

    @Override
    public void unassignTask(BigInteger taskId, BigInteger userId) throws TaskNotFoundException{
        assignmentRepository.deleteByTask_TaskIdAndUserId(taskId, userId);
    }

    @Override
    public void assignTaskToAll(List<BigInteger> userIds, BigInteger taskId) {

    }

    @Override
    public void unassignTaskFromAll(List<BigInteger> userIds, BigInteger taskId) {

    }

    @Override
    public Page<Task> getUserAssignedTasks(BigInteger userId, int page, int size) {
        Pageable pageable = PageRequest.of(0, 20);
        return assignmentRepository.findAllByUserId(userId,pageable);
    }
}
