package com.pilatesstudio.identity.controller;

import com.pilatesstudio.identity.dto.AccountRoleDto;
import com.pilatesstudio.identity.dto.RoleDto;
import com.pilatesstudio.identity.service.AccountRoleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account-roles")
@RequiredArgsConstructor
public class AccountRoleController {

    private final AccountRoleService accountRoleService;

    @GetMapping("/{id}")
    public AccountRoleDto findById(@PathVariable Long id) {
        return accountRoleService.findById(id);
    }

    @GetMapping("/query")
    public List<AccountRoleDto> findAll() {
        return accountRoleService.findAll();
    }

    @GetMapping("/{accountId}")
    public List<RoleDto> findAllByAccountId(
            @PathVariable Long accountId
    ) {
        return accountRoleService.findRolesByAccountId(accountId);
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AccountRoleDto> create(
            @Valid @RequestBody AccountRoleDto accountRoleDto
    ) {
        return ResponseEntity.ok(
                accountRoleService.create(accountRoleDto)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> removeRole(
            @Valid @RequestBody AccountRoleDto accountRoleDto
    ) {
        accountRoleService.delete(accountRoleDto);
        return ResponseEntity.noContent().build();
    }
}