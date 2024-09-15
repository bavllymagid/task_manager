package com.tasks.task_management.local.exceptions;

public class AssignmentHappenedBeforeException extends RuntimeException{
    public AssignmentHappenedBeforeException(String message) {
        super(message);
    }
}
