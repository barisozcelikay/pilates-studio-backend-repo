package com.pilatesstudio.identity.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldAssignRole() throws Exception {

        AccountRoleDto dto = new AccountRoleDto();
        dto.setAccountId(1L);
        dto.setRoleId(1L);

        mockMvc.perform(
                post("/api/account-roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldNotAssignSameRoleTwice() throws Exception {

        AccountRoleDto dto = new AccountRoleDto();
        dto.setAccountId(1L);
        dto.setRoleId(2L);

        String body = objectMapper.writeValueAsString(dto);

        mockMvc.perform(
                post("/api/account-roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(status().isCreated());

        mockMvc.perform(
                post("/api/account-roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(status().isConflict());
    }

    @Test
    void shouldFindAccountRole() throws Exception {

        mockMvc.perform(
                get("/api/account-roles/1")
        ).andExpect(status().isOk());
    }

    @Test
    void shouldReturnAccountRoles() throws Exception {

        mockMvc.perform(
                get("/api/account-roles/account/1")
        ).andExpect(status().isOk());
    }

    @Test
    void shouldRemoveRole() throws Exception {

        // Önce yeni ilişki oluştur
        AccountRoleDto dto = new AccountRoleDto();
        dto.setAccountId(1L);
        dto.setRoleId(2L);

        String response = mockMvc.perform(
                        post("/api/account-roles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountRoleDto created =
                objectMapper.readValue(response, AccountRoleDto.class);

        mockMvc.perform(
                delete("/api/account-roles/" + created.getId())
        ).andExpect(status().isNoContent());
    }
}