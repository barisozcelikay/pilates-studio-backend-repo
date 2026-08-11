package com.pilatesstudio.authentication.entity;

import com.pilatesstudio.authentication.model.PasswordTokenType;
import com.pilatesstudio.common.entity.BaseEntity;
import com.pilatesstudio.identity.entity.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "password_token",
        schema = "pilates_studio"
)
public class PasswordToken extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "account_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false
    )
    private Account account;

    @Column(
            name = "account_id",
            nullable = false
    )
    private Long accountId;

    @Column(
            name = "token_hash",
            nullable = false,
            unique = true,
            length = 255
    )
    private String tokenHash;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "token_type",
            nullable = false,
            length = 30
    )
    private PasswordTokenType tokenType;

    @Column(
            name = "expires_at",
            nullable = false
    )
    private OffsetDateTime expiresAt;

    @Column(name = "used_at")
    private OffsetDateTime usedAt;
}