package com.pilatesstudio.menu.mapper;

import com.pilatesstudio.menu.dto.MenuDto;
import com.pilatesstudio.menu.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    @Mapping(
            target = "parentId",
            expression = "java(menu.getParent() != null ? menu.getParent().getId() : null)"
    )
    @Mapping(target = "children", expression = "java(new java.util.ArrayList<>())")
    MenuDto toDto(Menu menu);
}