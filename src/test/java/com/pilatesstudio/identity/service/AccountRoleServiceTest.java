package com.pilatesstudio.identity.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pilatesstudio.common.exception.BusinessException;
import com.pilatesstudio.common.exception.ResourceNotFoundException;
import com.pilatesstudio.identity.dto.AccountRoleDto;
import com.pilatesstudio.identity.dto.RoleDto;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class AccountRoleServiceTest {

    @Autowired
    private AccountRoleService accountRoleService;

    @Test
    void shouldAssignRoleToAccount() {

        AccountRoleDto dto = new AccountRoleDto();
        dto.setAccountId(4L);
        dto.setRoleId(2L);

        AccountRoleDto result =
                accountRoleService.assignRole(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(1L);
        assertThat(result.getRoleId()).isEqualTo(2L);
    }

    @Test
    void shouldNotAssignSameRoleTwice() {

        AccountRoleDto dto = new AccountRoleDto();
        dto.setAccountId(1L);
        dto.setRoleId(1L);

        accountRoleService.assignRole(dto);

        assertThatThrownBy(() ->
                accountRoleService.assignRole(dto)
        )
                .isInstanceOf(BusinessException.class)
                .hasMessage("Role is already assigned to account");
    }

    @Test
    void shouldAllowMultipleRolesForSameAccount() {

        AccountRoleDto firstRole = new AccountRoleDto();
        firstRole.setAccountId(2L);
        firstRole.setRoleId(2L);

        AccountRoleDto secondRole = new AccountRoleDto();
        secondRole.setAccountId(3L);
        secondRole.setRoleId(2L);

        accountRoleService.assignRole(firstRole);
        accountRoleService.assignRole(secondRole);

        List<RoleDto> roles =
                accountRoleService.findRolesByAccountId(2L);

        assertThat(roles)
                .hasSize(2)
                .extracting(RoleDto::getId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void shouldRemoveRole() {

        AccountRoleDto dto = new AccountRoleDto();
        dto.setAccountId(3L);
        dto.setRoleId(1L);

        AccountRoleDto created =
                accountRoleService.assignRole(dto);

        accountRoleService.removeRole(created.getId());

        assertThatThrownBy(() ->
                accountRoleService.findById(created.getId())
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }
}