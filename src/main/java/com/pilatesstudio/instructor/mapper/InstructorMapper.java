package com.pilatesstudio.instructor.mapper;

import com.pilatesstudio.instructor.dto.InstructorDto;
import com.pilatesstudio.instructor.entity.Instructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstructorMapper {

    @Mapping(target = "firstName", source = "account.firstName")
    @Mapping(target = "lastName", source = "account.lastName")
    @Mapping(target = "phone", source = "account.phone")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "status", expression = "java(instructor.getAccount().getStatus().name())")
    InstructorDto toDto(Instructor instructor);
}
