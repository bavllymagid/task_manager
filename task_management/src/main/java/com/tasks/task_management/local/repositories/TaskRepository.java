package com.tasks.task_management.local.repositories;

import com.tasks.task_management.local.models.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, BigInteger> {
    Page<Task> findTasksByUserId(BigInteger userId, Pageable pageable);
    Task findTaskByUserId(BigInteger userId);

    @Transactional
    void deleteByUserId(BigInteger userId);

    @Transactional
    @Query("update Task t set t.status = ?1 where t.userId = ?2")
    void updateStatusByUserId(String status, BigInteger userId);
}
