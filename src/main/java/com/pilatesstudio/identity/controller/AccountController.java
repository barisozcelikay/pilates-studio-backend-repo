package com.pilatesstudio.identity.controller;

import com.pilatesstudio.identity.dto.AccountDto;
import com.pilatesstudio.identity.service.AccountService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public List<AccountDto> findAll() {
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    public AccountDto findById(@PathVariable Long id) {
        return accountService.findById(id);
    }

    @PreAuthorize("hasAuthority('PROFILE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto create(
            @Valid @RequestBody AccountDto accountDto
    ) {
        return accountService.create(accountDto);
    }

    @PreAuthorize("hasAuthority('PROFILE_ADMIN')")
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto update(
            @Valid @RequestBody AccountDto accountDto
    ) {
        return accountService.update(accountDto);
    }

    @PreAuthorize("hasAuthority('PROFILE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        accountService.delete(id);
    }

    @GetMapping("/me")
    public AccountDto getCurrentAccount(Authentication authentication) {
        Long accountId = Long.valueOf(authentication.getName());

        return accountService.findById(accountId);
    }
}
