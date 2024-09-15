package com.tasks.task_management.local.repositories;

import com.tasks.task_management.local.models.Task;
import com.tasks.task_management.local.models.TaskAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface TaskAssRepository extends JpaRepository<TaskAssignment, BigInteger> {
    @Query("SELECT t FROM Task t JOIN TaskAssignment ta ON t.taskId = ta.task.taskId WHERE ta.userId = :userId")
    Page<Task> findAllByUserId(@Param("userId") BigInteger userId, Pageable pageable);
    void deleteByTask_TaskIdAndUserId(BigInteger taskId, BigInteger userId);
}
