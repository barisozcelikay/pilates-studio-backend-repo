package com.pilatesstudio.identity.mapper;

import com.pilatesstudio.identity.dto.ProfileDto;
import com.pilatesstudio.identity.entity.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDto toDto(Profile profile);

    Profile toEntity(ProfileDto profileDto);
}