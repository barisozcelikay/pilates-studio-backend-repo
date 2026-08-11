package com.pilatesstudio.identity.controller;

import com.pilatesstudio.identity.dto.RoleDto;
import com.pilatesstudio.identity.service.RoleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleDto> findAll() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public RoleDto findById(@PathVariable Long id) {
        return roleService.findById(id);
    }

    @GetMapping("/code/{code}")
    public RoleDto findByCode(@PathVariable String code) {
        return roleService.findByCode(code);
    }
}