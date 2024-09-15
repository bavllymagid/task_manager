package com.tasks.task_management.local.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_assignments", indexes = {
        @Index(name = "idx_task_assignments_task_id", columnList = "task_id"),
        @Index(name = "idx_task_assignments_user_id", columnList = "user_id"),
        @Index(name = "idx_task_assignments_assigned_by", columnList = "assigned_by")
})
@Data
public class TaskAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private BigInteger assignmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "user_id", nullable = false)
    private BigInteger userId;

    @Column(name = "assigned_by", nullable = false)
    private BigInteger assignedBy;

    @Column(name = "assigned_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime assignedAt;
}
