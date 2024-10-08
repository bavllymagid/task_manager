package com.tasks.task_management.local.repositories;

import com.tasks.task_management.local.models.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, BigInteger> {
    Page<Notification> findAllByUserId(BigInteger userId, Pageable pageable);
    boolean existsByUserIdAndTaskId(BigInteger userId, BigInteger taskId);
    @Transactional
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.createdAt < ?1")
    void deleteByCreatedAtBefore(LocalDateTime time);

    @Transactional
    @Modifying
    void deleteAllByUserId(BigInteger userId);

    boolean existsByUserId(BigInteger userId);
}
