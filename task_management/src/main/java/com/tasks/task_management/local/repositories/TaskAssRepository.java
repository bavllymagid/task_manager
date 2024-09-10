package com.tasks.task_management.local.repositories;

import com.tasks.task_management.local.models.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskAssRepository extends JpaRepository<TaskAssignment, BigInteger> {
    Optional<List<BigInteger>> findAllTasksIdByUserId(BigInteger userId);
}
