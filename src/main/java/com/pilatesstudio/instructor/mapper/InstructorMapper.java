package com.pilatesstudio.instructor.mapper;

import com.pilatesstudio.identity.entity.Account;
import com.pilatesstudio.instructor.dto.InstructorDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstructorMapper {

    @Mapping(target = "active", expression = "java(account.getStatus() == com.pilatesstudio.identity.model.AccountStatus.ACTIVE)")
    InstructorDto toDto(Account account);
}
