package com.pilatesstudio.authentication.jwt;

import com.pilatesstudio.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${jwt.expiration}")
    private long expiration;

    public long getExpiration() {
        return expiration;
    }

    public String generateToken(
            Long accountId,
            Collection<String> roles
    ) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("pilates-studio")
                .subject(accountId.toString())
                .issuedAt(now)
                .expiresAt(
                        now.plus(expiration, ChronoUnit.SECONDS)
                )
                .claim("roles", roles)
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(claims)
        ).getTokenValue();
    }

    public String generateRoleSelectionToken(
            Long accountId,
            Collection<String> roles
    ) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("pilates-studio")
                .subject(accountId.toString())
                .issuedAt(now)
                .expiresAt(
                        now.plus(5, ChronoUnit.MINUTES)
                )
                .claim("roles", roles)
                .claim("token_type", "ROLE_SELECTION")
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(claims)
        ).getTokenValue();
    }

    public Long getAccountIdFromRoleSelectionToken(
            String token
    ) {
        Jwt jwt = jwtDecoder.decode(token);

        String tokenType =
                jwt.getClaimAsString("token_type");

        if (!"ROLE_SELECTION".equals(tokenType)) {
            throw new BusinessException(
                    "Invalid role selection token"
            );
        }

        return Long.valueOf(
                jwt.getSubject()
        );
    }
}