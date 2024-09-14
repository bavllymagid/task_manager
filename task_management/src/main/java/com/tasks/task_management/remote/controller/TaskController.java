package com.tasks.task_management.remote.controller;

import com.tasks.task_management.local.exceptions.InvalidTokenException;
import com.tasks.task_management.local.exceptions.PassedDueDateException;
import com.tasks.task_management.local.models.Task;
import com.tasks.task_management.remote.dto.TaskDto;
import com.tasks.task_management.remote.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    TaskService taskService;
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<TaskDto> createTask(@RequestHeader("Authorization") String token,
                                              @RequestBody TaskDto task) throws InvalidTokenException, PassedDueDateException {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @PutMapping("/update")
    public ResponseEntity<Task> updateTask(@RequestHeader("Authorization") String token,
                                           @RequestBody Task task) {
        taskService.updateTask(task);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> deleteTask(@RequestHeader("Authorization") String token,
                                             @PathVariable BigInteger taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted");
    }

    @GetMapping("/get/{taskId}")
    public ResponseEntity<Task> getTask(@RequestHeader("Authorization") String token,
                                        @PathVariable BigInteger taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    @GetMapping("/get/user_tasks/{userId}")
    public ResponseEntity<List<Task>> getUserCreatedTasks(@RequestHeader("Authorization") String token,
                                                          @PathVariable BigInteger userId,
                                                          @RequestParam int page,
                                                          @RequestParam int size) {
        return ResponseEntity.ok(taskService.getUserCreatedTasks(userId, page, size).getContent());
    }

}
