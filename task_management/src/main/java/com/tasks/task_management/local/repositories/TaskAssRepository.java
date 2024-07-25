package com.tasks.task_management.local.repositories;

import com.tasks.task_management.local.models.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface TaskAssRepository extends JpaRepository<TaskAssignment, BigInteger> {
}
