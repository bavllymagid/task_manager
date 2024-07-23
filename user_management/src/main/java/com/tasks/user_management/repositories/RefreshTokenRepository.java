package com.tasks.user_management.repositories;

import com.tasks.user_management.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserEmail(String email);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
