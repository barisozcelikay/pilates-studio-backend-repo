package com.pilatesstudio.lesson.mapper;

import com.pilatesstudio.lesson.dto.LessonDto;
import com.pilatesstudio.lesson.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "instructorIds", ignore = true)
    @Mapping(target = "instructorNames", ignore = true)
    LessonDto toDto(Lesson lesson);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Lesson toEntity(LessonDto lessonDto);
}
