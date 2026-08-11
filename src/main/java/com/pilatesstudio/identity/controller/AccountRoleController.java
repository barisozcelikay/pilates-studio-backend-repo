package com.pilatesstudio.identity.controller;

import com.pilatesstudio.identity.dto.AccountRoleDto;
import com.pilatesstudio.identity.dto.RoleDto;
import com.pilatesstudio.identity.service.AccountRoleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountRoleDto assignRole(
            @Valid @RequestBody AccountRoleDto accountRoleDto
    ) {
        return accountRoleService.assignRole(accountRoleDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRole(@PathVariable Long id) {
        accountRoleService.removeRole(id);
    }
}