package com.pilatesstudio.identity.entity;

import com.pilatesstudio.identity.model.AccountStatus;
import com.pilatesstudio.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "account",
        schema = "pilates_studio"
)
@Getter
@Setter
@NoArgsConstructor
public class Account extends BaseEntity {

    @Column(
            name = "phone",
            nullable = false,
            unique = true,
            length = 20
    )
    private String phone;

    @Column(
            name = "email",
            unique = true,
            length = 255
    )
    private String email;

    @Column(
            name = "password_hash",
            length = 255
    )
    private String passwordHash;


    @Column(
            name = "first_name",
            nullable = false,
            length = 100
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            length = 100
    )
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private AccountStatus status;

    @Column(
            name = "email_verified",
            nullable = false
    )
    private boolean emailVerified;


    @Column(
            name = "profile_id",
            nullable = false
    )
    private Long profileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "profile_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_account_profile")
    )
    private Profile profile;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
}