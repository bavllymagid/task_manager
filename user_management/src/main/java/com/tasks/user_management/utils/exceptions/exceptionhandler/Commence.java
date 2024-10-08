package com.tasks.user_management.utils.exceptions.exceptionhandler;

import com.tasks.user_management.utils.exceptions.exceptionhandler.message.CommenceMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Commence {
    @ExceptionHandler
    public ResponseEntity<CommenceMessage> handleException(Exception e) {
        CommenceMessage commenceMessage = new CommenceMessage();
        commenceMessage.setTimestamp(System.currentTimeMillis());
        commenceMessage.setMessage(e.getMessage());
        commenceMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(commenceMessage.getStatus()).body(commenceMessage);
    }
}
