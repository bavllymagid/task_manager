package com.tasks.user_management.local.repositories;

import com.tasks.user_management.local.models.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserEmail(String email);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Transactional
    @Modifying
    @Query("delete from RefreshToken rt where rt.user.email = ?1")
    void deleteByUserEmail(String email);
}
