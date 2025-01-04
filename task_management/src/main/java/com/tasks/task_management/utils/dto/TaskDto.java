package com.tasks.task_management.utils.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private BigInteger taskId;
    private String title;
    private String description;
    private String status;
    private BigInteger userId;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
}
