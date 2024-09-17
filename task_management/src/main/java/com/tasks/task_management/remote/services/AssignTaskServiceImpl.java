package com.tasks.task_management.remote.services;

import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.local.exceptions.AssignmentHappenedBeforeException;
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
import java.util.ArrayList;
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
    public void assignTaskToAll(List<BigInteger> userIds, BigInteger taskId) throws TaskNotFoundException{
        List<TaskAssignment> taskAssignments = new ArrayList<>();
        for (BigInteger userId : userIds) {
            if(!assignmentRepository.existsByTask_TaskIdAndUserId(taskId, userId)) {
                TaskAssignment taskAssignment = new TaskAssignment();
                taskAssignment.setTask(taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found")));
                taskAssignment.setUserId(userId);
                taskAssignment.setAssignedBy(UserSingleton.getInstance().getId());
                taskAssignments.add(taskAssignment);
            }
        }
        assignmentRepository.saveAll(taskAssignments);
    }

    @Override
    public void unassignTaskFromAll(List<BigInteger> userIds, BigInteger taskId) throws TaskNotFoundException {
        for (BigInteger userId : userIds) {
            if(assignmentRepository.existsByTask_TaskIdAndUserId(taskId, userId)){
                assignmentRepository.deleteByTask_TaskIdAndUserId(taskId, userId);
            }
            else
                throw new TaskNotFoundException("Task not found");
        }
    }

    @Override
    public void unassignAllTasks(BigInteger userId) throws TaskNotFoundException {
        if(!assignmentRepository.existsByUserId(userId))
            throw new TaskNotFoundException("Task not found");
        assignmentRepository.deleteByUserId(userId);
    }

    @Override
    public Page<Task> getUserAssignedTasks(BigInteger userId, int page, int size) {
        Pageable pageable = PageRequest.of(0, 20);
        return assignmentRepository.findAllByUserId(userId,pageable);
    }
}
