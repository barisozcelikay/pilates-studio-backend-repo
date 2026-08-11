package com.pilatesstudio.identity.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @Test
    void shouldFindRoleById() {

        //Role role = roleService.findById(1L);

        //assertThat(role).isNotNull();
        //assertThat(role.getId()).isEqualTo(1L);
    }

    @Test
    void shouldFindRoleByCode() {

        //Role role = roleService.findByCode("ADMIN");

        //assertThat(role).isNotNull();
        //assertThat(role.getCode()).isEqualTo("ADMIN");
    }
}