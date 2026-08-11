package com.pilatesstudio.identity.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldFindAllRoles() throws Exception {

        mockMvc.perform(
                get("/api/roles")
        ).andExpect(status().isOk());
    }

    @Test
    void shouldFindRoleById() throws Exception {

        mockMvc.perform(
                get("/api/roles/1")
        ).andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenRoleDoesNotExist() throws Exception {

        mockMvc.perform(
                get("/api/roles/999999")
        ).andExpect(status().isNotFound());
    }
}