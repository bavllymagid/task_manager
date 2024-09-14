package com.tasks.task_management.local.exceptions.handler;

import com.tasks.task_management.local.exceptions.InvalidToken;
import com.tasks.task_management.local.exceptions.body.Commence;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskExceptionHandler {
    @ExceptionHandler(InvalidToken.class)
    public ResponseEntity<Commence> handleInvalidTokenException(InvalidToken e) {
        Commence commence = new Commence(e.getMessage(), HttpStatus.UNAUTHORIZED.value(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(commence);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Commence> handleException(Exception e) {
        Commence commence = new Commence(e.getMessage(), HttpStatus.BAD_REQUEST.value(), System.currentTimeMillis());
        return ResponseEntity.badRequest().body(commence);
    }
}
