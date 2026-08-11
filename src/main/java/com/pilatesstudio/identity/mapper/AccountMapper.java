package com.pilatesstudio.identity.mapper;

import com.pilatesstudio.identity.dto.AccountDto;
import com.pilatesstudio.identity.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "password", ignore = true)
    AccountDto toDto(Account account);

    @Mapping(target = "passwordHash", ignore = true)
    Account toEntity(AccountDto accountDto);
}