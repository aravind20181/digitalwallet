package com.digitalwallet.digitalwallet.repository;

import com.digitalwallet.digitalwallet.model.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken,Long> {
    boolean existsByToken(String token);
}

