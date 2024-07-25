package com.tasks.task_management.local.repositories;

import com.tasks.task_management.local.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface TaskRepository extends JpaRepository<Task, BigInteger> {
}
