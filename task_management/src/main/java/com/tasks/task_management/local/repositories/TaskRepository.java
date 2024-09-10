package com.tasks.task_management.local.repositories;

import com.tasks.task_management.local.models.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, BigInteger> {
    @Query("select * from Task t JOIN TaskAssignment ta ON t.taskId = ta.taskId where ta.userId = ?1")
    Optional<List<Task>> getTasksByUserId(BigInteger userId);

    Optional<List<Task>> findAllByUserId(BigInteger userId);

    @Transactional
    void updateTaskStatusByTaskId(BigInteger taskId, String status);
}
