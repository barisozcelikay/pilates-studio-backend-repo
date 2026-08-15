package com.pilatesstudio.menu.controller;

import com.pilatesstudio.menu.dto.MenuDto;
import com.pilatesstudio.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public List<MenuDto> findMenus(Authentication authentication) {

        String profileCode = authentication.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .filter(authority -> authority.startsWith("PROFILE_"))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("Active profile not found")
                );

        return menuService.findActiveMenusByProfileCode(profileCode);
    }


}