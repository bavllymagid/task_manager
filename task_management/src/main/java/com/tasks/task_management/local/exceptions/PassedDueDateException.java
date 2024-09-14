package com.tasks.task_management.local.exceptions;

public class PassedDueDateException extends RuntimeException {
    public PassedDueDateException(String message) {
        super(message);
    }
}
