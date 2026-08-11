package com.pilatesstudio.authentication.repository;

import com.pilatesstudio.authentication.entity.PasswordToken;
import com.pilatesstudio.authentication.model.PasswordTokenType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordTokenRepository
        extends JpaRepository<PasswordToken, Long> {

    Optional<PasswordToken> findByTokenHash(String tokenHash);

    Optional<PasswordToken> findByTokenHashAndTokenType(
            String tokenHash,
            PasswordTokenType tokenType
    );
}