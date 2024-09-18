package com.tasks.task_management.local.repositories;

import com.tasks.task_management.local.models.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, BigInteger> {
    Page<Notification> findByUserId(BigInteger userId, Pageable pageable);
    boolean existsByUserIdAndTaskId(BigInteger userId, BigInteger taskId);
}
