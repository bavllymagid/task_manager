package com.tasks.user_management.repositories;

import com.tasks.user_management.models.UserRole;
import com.tasks.user_management.models.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface UserRolesRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByUserId(BigInteger userId);
}
