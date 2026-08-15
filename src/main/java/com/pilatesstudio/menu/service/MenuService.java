package com.pilatesstudio.menu.service;

import com.pilatesstudio.menu.dto.MenuDto;
import com.pilatesstudio.menu.entity.Menu;
import com.pilatesstudio.menu.mapper.MenuMapper;
import com.pilatesstudio.menu.repository.MenuProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuProfileRepository menuProfileRepository;
    private final MenuMapper menuMapper;

    public List<MenuDto> findActiveMenusByProfileCode(String profileCode) {

        List<Menu> menus =
                menuProfileRepository.findActiveMenusByProfileCode(profileCode);

        Map<Long, MenuDto> menuMap = menus.stream()
                .map(menuMapper::toDto)
                .collect(Collectors.toMap(
                        MenuDto::getId,
                        Function.identity()
                ));

        List<MenuDto> rootMenus = new ArrayList<>();

        for (MenuDto menu : menuMap.values()) {

            if (menu.getParentId() == null) {
                rootMenus.add(menu);
                continue;
            }

            MenuDto parent = menuMap.get(menu.getParentId());

            if (parent != null) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }

                parent.getChildren().add(menu);
            }
        }

        rootMenus.sort(
                java.util.Comparator.comparing(MenuDto::getSortOrder)
        );

        rootMenus.forEach(root ->
                root.getChildren().sort(
                        java.util.Comparator.comparing(MenuDto::getSortOrder)
                )
        );

        return rootMenus;
    }



}