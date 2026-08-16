package com.pilatesstudio.menu.controller;

import com.pilatesstudio.menu.dto.MenuDto;
import com.pilatesstudio.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/settings")
    @PreAuthorize("hasAuthority('PROFILE_ADMIN')")
    public List<MenuDto> findAllMenus() {
        return menuService.findAll();
    }

    @PutMapping()
    public MenuDto update(
            @RequestBody MenuDto request
    ) {
        return menuService.update(request);
    }


}