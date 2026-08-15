package com.pilatesstudio.menu.entity;

import com.pilatesstudio.common.entity.BaseEntity;
import com.pilatesstudio.identity.entity.Profile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "menu_profile",
        schema = "pilates_studio",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_menu_profile_menu_profile",
                        columnNames = {"menu_id", "profile_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class MenuProfile extends BaseEntity {

    @Column(
            name = "menu_id",
            nullable = false
    )
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "menu_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_menu_profile_menu")
    )
    private Menu menu;

    @Column(
            name = "profile_id",
            nullable = false
    )
    private Long profileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "profile_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_menu_profile_profile")
    )
    private Profile profile;
}