package com.tasks.task_management.local.exceptions.handler;

import com.tasks.task_management.local.exceptions.body.Commence;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TaskExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Commence> handleException(Exception e) {
        Commence commence = new Commence(e.getMessage(), 400, System.currentTimeMillis());
        return ResponseEntity.badRequest().body(commence);
    }
}
