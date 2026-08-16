package com.pilatesstudio.menu.mapper;

import com.pilatesstudio.identity.dto.ProfileDto;
import com.pilatesstudio.identity.entity.Profile;
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
    @Mapping(
            target = "children",
            expression = "java(new java.util.ArrayList<>())"
    )
    MenuDto toDto(Menu menu);

    @Mapping(target = "id", source = "id")
    @Mapping(
            target = "parent",
            expression = "java(menuDto.getParentId() != null ? createParentReference(menuDto.getParentId()) : null)"
    )
    Menu toEntity(MenuDto menuDto);

    default Menu createParentReference(Long parentId) {
        Menu parent = new Menu();
        parent.setId(parentId);
        return parent;
    }
}
