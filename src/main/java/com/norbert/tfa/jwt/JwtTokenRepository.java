package com.norbert.tfa.jwt;

import com.norbert.tfa.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken,Long> {
    @Transactional(readOnly = true)
    @Query("SELECT s FROM JwtToken s WHERE s.token = ?1")
    Optional<JwtToken> findByToken(String token);
}
