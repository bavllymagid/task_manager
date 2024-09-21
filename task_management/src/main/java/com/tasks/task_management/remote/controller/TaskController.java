package com.tasks.task_management.remote.controller;

import com.tasks.task_management.local.StaticObjects.UserSingleton;
import com.tasks.task_management.local.exceptions.InvalidTokenException;
import com.tasks.task_management.local.exceptions.PassedDueDateException;
import com.tasks.task_management.local.exceptions.TaskNotFoundException;
import com.tasks.task_management.local.models.Task;
import com.tasks.task_management.remote.dto.TaskDto;
import com.tasks.task_management.remote.services.AssignTaskService;
import com.tasks.task_management.remote.services.TaskService;
import com.tasks.task_management.remote.utils.requests.Requests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    TaskService taskService;
    AssignTaskService assignTaskService;
    @Autowired
    public TaskController(TaskService taskService,
                          AssignTaskService assignTaskService) {
        this.taskService = taskService;
        this.assignTaskService = assignTaskService;
    }

    @PostMapping("/create")
    public ResponseEntity<TaskDto> createTask(@RequestHeader("Authorization") String token,
                                              @RequestBody TaskDto task) throws InvalidTokenException, PassedDueDateException {
        if(!Requests.changeRole(token, UserSingleton.getInstance().getEmail(), "ADMIN")) {
            throw new InvalidTokenException("Invalid token");
        }
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @PutMapping("/update")
    public ResponseEntity<TaskDto> updateTask(@RequestHeader("Authorization") String token,
                                           @RequestBody TaskDto task) {
        taskService.updateTask(task);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> deleteTask(@RequestHeader("Authorization") String token,
                                             @PathVariable BigInteger taskId) throws TaskNotFoundException {
        taskService.deleteTask(taskId, token);
        return ResponseEntity.ok("Task deleted");
    }

    @GetMapping("/get/{taskId}")
    public ResponseEntity<Task> getTask(@RequestHeader("Authorization") String token,
                                        @PathVariable BigInteger taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    @GetMapping("/get/user_tasks/{userId}")
    public ResponseEntity<Page<Task>> getUserCreatedTasks(@RequestHeader("Authorization") String token,
                                                          @PathVariable BigInteger userId,
                                                          @RequestParam int page,
                                                          @RequestParam int size) {
        return ResponseEntity.ok(taskService.getUserCreatedTasks(userId, page, size));
    }

    @GetMapping("/get/user_assigned_tasks/{userId}")
    public ResponseEntity<Page<Task>> getUserAssignedTasks(@RequestHeader("Authorization") String token,
                                                           @PathVariable BigInteger userId,
                                                           @RequestParam int page,
                                                           @RequestParam int size) {
        return ResponseEntity.ok(assignTaskService.getUserAssignedTasks(userId, page, size));
    }

    @PostMapping("/assign/{taskId}")
    public ResponseEntity<String> assignTaskToAll(@RequestHeader("Authorization") String token,
                                                  @PathVariable BigInteger taskId,
                                                  @RequestBody List<BigInteger> userIds) {
        assignTaskService.assignTaskToAll(userIds, taskId);
        return ResponseEntity.ok("Task assigned to all");
    }

    @DeleteMapping("/unassign/{taskId}")
    public ResponseEntity<String> unassignTaskFromAll(@RequestHeader("Authorization") String token,
                                                      @PathVariable BigInteger taskId,
                                                      @RequestBody List<BigInteger> userIds) {
        assignTaskService.unassignTaskFromAll(userIds, taskId);
        return ResponseEntity.ok("Task unassigned from all");
    }

    @DeleteMapping("/unassign/all/{userId}")
    public ResponseEntity<String> unassignAllTasks(@RequestHeader("Authorization") String token,
                                                   @PathVariable BigInteger userId) {
        assignTaskService.unassignAllTasks(userId);
        return ResponseEntity.ok("All tasks unassigned");
    }

}
