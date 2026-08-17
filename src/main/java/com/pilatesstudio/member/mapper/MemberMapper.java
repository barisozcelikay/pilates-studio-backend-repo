package com.pilatesstudio.member.mapper;

import com.pilatesstudio.identity.entity.Account;
import com.pilatesstudio.member.dto.MemberDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(target = "active", expression = "java(account.getStatus() == com.pilatesstudio.identity.model.AccountStatus.ACTIVE)")
    MemberDto toDto(Account account);
}
