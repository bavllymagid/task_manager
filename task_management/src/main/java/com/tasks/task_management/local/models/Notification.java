package com.tasks.task_management.local.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private BigInteger notificationId;

    @Column(name = "user_id", nullable = false)
    private BigInteger userId;

    @Column(name = "task_id", nullable = false)
    private BigInteger taskId;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "read", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean read;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;
}
