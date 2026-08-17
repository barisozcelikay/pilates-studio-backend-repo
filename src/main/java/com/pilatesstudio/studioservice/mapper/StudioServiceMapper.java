package com.pilatesstudio.studioservice.mapper;
import com.pilatesstudio.studioservice.dto.StudioServiceDto;
import com.pilatesstudio.studioservice.entity.StudioService;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring") public interface StudioServiceMapper { StudioServiceDto toDto(StudioService entity); StudioService toEntity(StudioServiceDto dto); }
