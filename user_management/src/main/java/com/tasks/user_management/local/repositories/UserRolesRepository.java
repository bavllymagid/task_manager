package com.tasks.user_management.local.repositories;

import com.tasks.user_management.local.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface UserRolesRepository extends JpaRepository<UserRole, BigInteger> {
    UserRole findByName(String role);
}
