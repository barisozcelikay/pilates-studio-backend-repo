package com.pilatesstudio.authentication.service;

import com.pilatesstudio.authentication.dto.SetPasswordRequest;
import com.pilatesstudio.authentication.entity.PasswordToken;
import com.pilatesstudio.authentication.model.PasswordTokenType;
import com.pilatesstudio.common.exception.ResourceNotFoundException;
import com.pilatesstudio.identity.entity.Account;
import com.pilatesstudio.identity.model.AccountStatus;
import com.pilatesstudio.identity.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SetPasswordService {

    private final PasswordTokenService passwordTokenService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void setPassword(SetPasswordRequest request) {

        PasswordToken passwordToken =
                passwordTokenService.validateToken(
                        request.getToken(),
                        PasswordTokenType.INITIAL_PASSWORD
                );

        Account account =
                accountRepository.findById(
                                passwordToken.getAccountId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found: "
                                                + passwordToken.getAccountId()
                                )
                        );

        account.setPasswordHash(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        account.setStatus(AccountStatus.ACTIVE);
        account.setEmailVerified(true);

        accountRepository.save(account);

        passwordTokenService.markAsUsed(passwordToken);
    }
}