package com.pilatesstudio.instructor.mapper;

import com.pilatesstudio.instructor.dto.InstructorDto;
import com.pilatesstudio.instructor.entity.Instructor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstructorMapper {

    InstructorDto toDto(Instructor instructor);
}
