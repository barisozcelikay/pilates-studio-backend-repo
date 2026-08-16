package com.pilatesstudio.menu.service;

import com.pilatesstudio.common.exception.ResourceNotFoundException;
import com.pilatesstudio.menu.dto.MenuDto;
import com.pilatesstudio.menu.entity.Menu;
import com.pilatesstudio.menu.entity.MenuProfile;
import com.pilatesstudio.menu.mapper.MenuMapper;
import com.pilatesstudio.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuProfileService menuProfileService;
    private final MenuMapper menuMapper;

    public List<MenuDto> findActiveMenusByProfileCode(String profileCode) {

        List<Menu> menus =
                menuProfileService
                        .findActiveMenusByProfileCode(profileCode);

        List<MenuDto> menuDtos = menus.stream()
                .map(menuMapper::toDto)
                .toList();

        return buildTree(menuDtos);
    }

    public List<MenuDto> findAll() {

        List<Menu> menus = menuRepository.findAll();

        List<MenuProfile> menuProfiles =
                menuProfileService.findAll();

        Map<Long, List<Long>> profileIdsByMenuId =
                menuProfiles.stream()
                        .collect(Collectors.groupingBy(
                                menuProfile -> menuProfile.getMenu().getId(),
                                Collectors.mapping(
                                        MenuProfile::getProfileId,
                                        Collectors.toList()
                                )
                        ));

        List<MenuDto> menuDtos = menus.stream()
                .map(menu -> {
                    MenuDto dto = menuMapper.toDto(menu);

                    dto.setProfileIds(
                            profileIdsByMenuId.getOrDefault(
                                    menu.getId(),
                                    new ArrayList<>()
                            )
                    );

                    return dto;
                })
                .toList();

        return buildTree(menuDtos);
    }

    @Transactional
    public MenuDto update(MenuDto request) {

        Menu menu = menuRepository.findById(request.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Menu not found")
                );


        menuRepository.save(menuMapper.toEntity(request));

        menuProfileService.updateProfiles(
                request.getId(),
                request.getProfileIds()
        );

        MenuDto response = menuMapper.toDto(menu);
        response.setProfileIds(
                menuProfileService.findProfileIdsByMenuId(request.getId())
        );

        return response;
    }

    private List<MenuDto> buildTree(List<MenuDto> menuDtos) {

        Map<Long, MenuDto> menuMap = menuDtos.stream()
                .collect(Collectors.toMap(
                        MenuDto::getId,
                        Function.identity()
                ));

        List<MenuDto> rootMenus = new ArrayList<>();

        for (MenuDto menu : menuDtos) {

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

        Comparator<MenuDto> sortComparator =
                Comparator.comparing(
                        MenuDto::getSortOrder,
                        Comparator.nullsLast(Integer::compareTo)
                );

        rootMenus.sort(sortComparator);

        rootMenus.forEach(root -> {
            if (root.getChildren() != null) {
                root.getChildren().sort(sortComparator);
            }
        });

        return rootMenus;
    }
}