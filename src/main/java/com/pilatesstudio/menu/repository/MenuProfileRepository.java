package com.pilatesstudio.menu.repository;

import com.pilatesstudio.menu.entity.Menu;
import com.pilatesstudio.menu.entity.MenuProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuProfileRepository extends JpaRepository<MenuProfile, Long> {

    @Query("""
        SELECT DISTINCT m
        FROM Menu m
        WHERE m.active = true
          AND (
              EXISTS (
                  SELECT 1
                  FROM MenuProfile mp
                  WHERE mp.menu = m
                    AND mp.profile.code = :profileCode
              )
              OR EXISTS (
                  SELECT 1
                  FROM Menu child
                  JOIN MenuProfile childProfile ON childProfile.menu = child
                  WHERE child.parent = m
                    AND child.active = true
                    AND childProfile.profile.code = :profileCode
              )
          )
        ORDER BY m.sortOrder
        """)
    List<Menu> findActiveMenusByProfileCode(
            @Param("profileCode") String profileCode
    );

    List<MenuProfile> findAllByMenuId(Long menuId);

    void deleteAllByMenuId(Long menuId);


}