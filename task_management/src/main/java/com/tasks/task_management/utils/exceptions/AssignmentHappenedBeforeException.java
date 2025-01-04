package com.tasks.task_management.utils.exceptions;

public class AssignmentHappenedBeforeException extends RuntimeException{
    public AssignmentHappenedBeforeException(String message) {
        super(message);
    }
}
