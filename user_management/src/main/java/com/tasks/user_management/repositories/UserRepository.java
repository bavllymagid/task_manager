package com.tasks.user_management.repositories;

import com.tasks.user_management.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, BigInteger>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("select u.secretToken from User u where u.email = ?1")
    String findSecretTokenByEmail(String email);
    @Transactional
    @Modifying
    @Query("update User u set u.secretToken = ?2 where u.email = ?1")
    void updateSecretTokenByEmail(String email, String secretToken);
}
