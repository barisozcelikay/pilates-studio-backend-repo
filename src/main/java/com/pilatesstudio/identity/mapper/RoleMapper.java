package com.pilatesstudio.identity.mapper;

import com.pilatesstudio.identity.dto.RoleDto;
import com.pilatesstudio.identity.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto toDto(Role role);

    Role toEntity(RoleDto roleDto);
}