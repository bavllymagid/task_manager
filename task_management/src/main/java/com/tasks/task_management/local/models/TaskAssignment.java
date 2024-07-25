package com.tasks.task_management.local.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_assignments")
@Data
public class TaskAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private BigInteger assignmentId;

    @Column(name = "task_id", nullable = false)
    private BigInteger taskId;

    @Column(name = "user_id", nullable = false)
    private BigInteger userId;

    @Column(name = "assigned_by", nullable = false)
    private BigInteger assignedBy;

    @Column(name = "assigned_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime assignedAt;
}
