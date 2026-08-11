package com.pilatesstudio.identity.service;

import com.pilatesstudio.authentication.model.PasswordTokenType;
import com.pilatesstudio.authentication.service.EmailService;
import com.pilatesstudio.authentication.service.PasswordTokenService;
import com.pilatesstudio.common.exception.BusinessException;
import com.pilatesstudio.common.exception.ResourceNotFoundException;
import com.pilatesstudio.identity.dto.AccountDto;
import com.pilatesstudio.identity.dto.AccountRoleDto;
import com.pilatesstudio.identity.entity.Account;
import com.pilatesstudio.identity.entity.AccountRole;
import com.pilatesstudio.identity.mapper.AccountMapper;
import com.pilatesstudio.identity.model.AccountStatus;
import com.pilatesstudio.identity.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AccountRoleService accountRoleService;
    private final PasswordTokenService passwordTokenService;
    private final EmailService emailService;

    public List<AccountDto> findAll() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toDto)
                .toList();
    }

    public AccountDto findById(Long id) {
        return accountRepository.findById(id)
                .map(accountMapper::toDto)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + id
                        ));
    }

    public AccountDto findByPhone(String phone) {
        return accountRepository.findByPhone(phone)
                .map(accountMapper::toDto)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + phone
                        ));
    }

    @Transactional(readOnly = true)
    public Account findEntityByPhone(String phone) {
        return accountRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new BusinessException("Invalid phone")
                );
    }

    @Transactional(readOnly = true)
    public Account findEntityByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BusinessException("Invalid email")
                );
    }

    public AccountDto findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .map(accountMapper::toDto)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + email
                        ));
    }

    @Transactional
    public AccountDto create(AccountDto accountDto) {

        if (accountRepository.existsByPhone(accountDto.getPhone())) {
            throw new BusinessException(
                    "Phone already exists: " + accountDto.getPhone()
            );
        }

        if (accountDto.getEmail() != null
                && accountRepository.existsByEmail(accountDto.getEmail())) {

            throw new BusinessException(
                    "Email already exists: " + accountDto.getEmail()
            );
        }
        Account account = accountMapper.toEntity(accountDto);
        account.setStatus(AccountStatus.PENDING);
        account.setEmailVerified(false);
        account.setPasswordHash(null);

        Account savedAccount = accountRepository.save(account);
        accountRoleService.assignMemberRoleToNewAccount(account.getId());

        String token = passwordTokenService.createToken(
                savedAccount,
                PasswordTokenType.INITIAL_PASSWORD
        );

        emailService.sendInitialPasswordEmail(
                savedAccount.getEmail(),
                savedAccount.getFirstName(),
                savedAccount.getLastName(),
                token
        );

        return accountMapper.toDto(savedAccount);
    }
}