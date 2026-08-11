package com.pilatesstudio.identity.controller;

import com.pilatesstudio.identity.dto.AccountDto;
import com.pilatesstudio.identity.service.AccountService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/phone/{phone}")
    public AccountDto findByPhone(@PathVariable String phone) {
        return accountService.findByPhone(phone);
    }

    @GetMapping("/email/{email}")
    public AccountDto findByEmail(@PathVariable String email) {
        return accountService.findByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto create(
            @Valid @RequestBody AccountDto accountDto
    ) {
        return accountService.create(accountDto);
    }
}