package com.pilatesstudio.menu.repository;

import com.pilatesstudio.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {


}