package com.pilatesstudio.authentication.service;

import com.pilatesstudio.authentication.entity.PasswordToken;
import com.pilatesstudio.authentication.model.PasswordTokenType;
import com.pilatesstudio.authentication.repository.PasswordTokenRepository;
import com.pilatesstudio.common.exception.BusinessException;
import com.pilatesstudio.identity.entity.Account;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.HexFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordTokenService {

    private final PasswordTokenRepository passwordTokenRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public String createToken(
            Account account,
            PasswordTokenType tokenType
    ) {
        String rawToken = generateToken();

        PasswordToken passwordToken = new PasswordToken();

        passwordToken.setAccountId(account.getId());
        passwordToken.setTokenHash(hashToken(rawToken));
        passwordToken.setTokenType(tokenType);
        passwordToken.setExpiresAt(
                OffsetDateTime.now().plusHours(24)
        );

        passwordTokenRepository.save(passwordToken);

        return rawToken;
    }

    @Transactional(readOnly = true)
    public PasswordToken validateToken(
            String rawToken,
            PasswordTokenType tokenType
    ) {
        String tokenHash = hashToken(rawToken);

        PasswordToken passwordToken =
                passwordTokenRepository
                        .findByTokenHashAndTokenType(
                                tokenHash,
                                tokenType
                        )
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Invalid password token"
                                )
                        );

        if (passwordToken.getUsedAt() != null) {
            throw new BusinessException(
                    "Password token has already been used"
            );
        }

        if (passwordToken.getExpiresAt()
                .isBefore(OffsetDateTime.now())) {
            throw new BusinessException(
                    "Password token has expired"
            );
        }

        return passwordToken;
    }

    @Transactional
    public void markAsUsed(PasswordToken passwordToken) {
        passwordToken.setUsedAt(OffsetDateTime.now());
        passwordTokenRepository.save(passwordToken);
    }

    private String generateToken() {

        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        return HexFormat.of().formatHex(bytes);
    }

    private String hashToken(String token) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 algorithm is not available",
                    e
            );
        }
    }
}