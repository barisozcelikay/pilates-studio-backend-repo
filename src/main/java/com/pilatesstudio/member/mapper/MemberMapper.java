package com.pilatesstudio.member.mapper;

import com.pilatesstudio.member.dto.MemberDto;
import com.pilatesstudio.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(target = "firstName", source = "account.firstName")
    @Mapping(target = "lastName", source = "account.lastName")
    @Mapping(target = "phone", source = "account.phone")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "status", expression = "java(member.getAccount().getStatus().name())")
    MemberDto toDto(Member member);
}
