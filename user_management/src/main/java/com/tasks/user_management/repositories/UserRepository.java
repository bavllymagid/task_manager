package com.tasks.user_management.repositories;

import com.tasks.user_management.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, BigInteger>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
