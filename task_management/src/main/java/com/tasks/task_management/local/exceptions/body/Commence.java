package com.tasks.task_management.local.exceptions.body;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Commence {
    private String message;
    private int status;
    private long timestamp;
}
