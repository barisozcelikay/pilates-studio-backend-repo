package com.pilatesstudio.identity.entity;

import com.pilatesstudio.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "role",
        schema = "pilates_studio"
)
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity {

    @Column(
            name = "code",
            nullable = false,
            unique = true,
            length = 50
    )
    private String code;

    @Column(
            name = "name",
            nullable = false,
            length = 100
    )
    private String name;
}