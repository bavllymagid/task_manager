package com.tasks.user_management.repositories;

import com.tasks.user_management.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface UserRolesRepository extends JpaRepository<UserRole, BigInteger> {
    List<UserRole> findByUserId(BigInteger userId);
}
