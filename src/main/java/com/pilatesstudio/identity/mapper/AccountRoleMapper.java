package com.pilatesstudio.identity.mapper;

import com.pilatesstudio.identity.dto.AccountRoleDto;
import com.pilatesstudio.identity.entity.AccountRole;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {
                AccountMapper.class,
                RoleMapper.class
        }
)
public interface AccountRoleMapper {

    AccountRoleDto toDto(AccountRole accountRole);

    AccountRole toEntity(AccountRoleDto accountRoleDto);
}