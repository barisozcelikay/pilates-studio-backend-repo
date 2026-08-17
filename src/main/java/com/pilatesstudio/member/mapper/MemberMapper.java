package com.pilatesstudio.member.mapper;

import com.pilatesstudio.member.dto.MemberDto;
import com.pilatesstudio.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberDto toDto(Member member);
}
