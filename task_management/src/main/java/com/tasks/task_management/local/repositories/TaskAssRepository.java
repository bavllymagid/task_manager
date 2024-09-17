package com.tasks.task_management.local.repositories;

import com.tasks.task_management.local.models.Task;
import com.tasks.task_management.local.models.TaskAssignment;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface TaskAssRepository extends JpaRepository<TaskAssignment, BigInteger> {
    @Query("SELECT t FROM Task t JOIN TaskAssignment ta ON t.taskId = ta.task.taskId WHERE ta.userId = :userId")
    Page<Task> findAllByUserId(@Param("userId") BigInteger userId, Pageable pageable);
    @Transactional
    void deleteByTask_TaskIdAndUserId(BigInteger taskId, BigInteger userId);
    boolean existsByTask_TaskIdAndUserId(BigInteger taskId, BigInteger userId);
    @Transactional
    void deleteByUserId(BigInteger userId);
    @Query("SELECT ta.task FROM TaskAssignment ta WHERE ta.userId = :userId")
    List<Task> getAllByUserId(BigInteger userId);
    boolean existsByUserId(BigInteger userId);
    @Query("SELECT ta.userId FROM TaskAssignment ta WHERE ta.task.taskId = :taskId")
    List<BigInteger> findAllUserIdByTask_TaskId(BigInteger taskId);

}
