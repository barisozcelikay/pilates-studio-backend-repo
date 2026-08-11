package com.pilatesstudio.identity.entity;

import com.pilatesstudio.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "account_role",
        schema = "pilates_studio"
)
@Getter
@Setter
@NoArgsConstructor
public class AccountRole extends BaseEntity {

    @Column(
            name = "account_id",
            nullable = false
    )
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "account_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_account_role_account")
    )
    private Account account;

    @Column(
            name = "role_id",
            nullable = false
    )
    private Long roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "role_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_account_role_role")

    )
    private Role role;
}