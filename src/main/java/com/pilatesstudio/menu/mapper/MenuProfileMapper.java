package com.pilatesstudio.menu.mapper;

import com.pilatesstudio.menu.dto.MenuProfileDto;
import com.pilatesstudio.menu.entity.MenuProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuProfileMapper {


    MenuProfileDto toDto(MenuProfile menu);

    MenuProfile toEntity(MenuProfileDto menuProfileDto);
}