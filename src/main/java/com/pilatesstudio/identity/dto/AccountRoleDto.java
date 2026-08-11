package com.pilatesstudio.identity.dto;

import com.pilatesstudio.common.dto.BaseDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRoleDto extends BaseDto {

    @NotNull(message = "Account id is required")
    private Long accountId;

    private AccountDto account;

    @NotNull(message = "Role id is required")
    private Long roleId;

    private RoleDto role;
}