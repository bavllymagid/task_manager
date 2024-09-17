package com.tasks.task_management.local.repositories;

import com.tasks.task_management.local.models.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, BigInteger> {
    Page<Task> findTasksByUserId(BigInteger userId, Pageable pageable);
    Task findTaskByUserId(BigInteger userId);

    @Transactional
    void deleteByUserId(BigInteger userId);

    @Transactional
    @Query("update Task t set t.status = ?1 where t.userId = ?2")
    void updateStatusByUserId(String status, BigInteger userId);

    @Transactional
    @Query("update Task t set t.status = ?1 where t.taskId = ?2")
    void updateStatusByTaskId(String status, BigInteger taskId);

    @Query("SELECT t FROM Task t WHERE t.dueDate <= :nextDay AND t.dueDate >= :now AND t.userId = :userId")
    List<Task> findTasksDueWithinADayByUserId(@Param("now") LocalDateTime now,
                                              @Param("nextDay") LocalDateTime nextDay,
                                              @Param("userId") BigInteger userId);
}
