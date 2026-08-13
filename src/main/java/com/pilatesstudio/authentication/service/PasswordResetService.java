package com.pilatesstudio.authentication.service;

import com.pilatesstudio.authentication.dto.ForgotPasswordRequest;
import com.pilatesstudio.authentication.dto.ResetPasswordRequest;
import com.pilatesstudio.authentication.entity.PasswordToken;
import com.pilatesstudio.authentication.model.PasswordTokenType;
import com.pilatesstudio.authentication.repository.PasswordTokenRepository;
import com.pilatesstudio.common.exception.BusinessException;
import com.pilatesstudio.common.exception.ResourceNotFoundException;
import com.pilatesstudio.identity.entity.Account;
import com.pilatesstudio.identity.repository.AccountRepository;
import com.pilatesstudio.identity.model.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final AccountRepository accountRepository;
    private final PasswordTokenService passwordTokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {

        Account account =
                accountRepository.findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found"
                                )
                        );

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException(
                    "Account is not active"
            );
        }

        String token =
                passwordTokenService.createToken(
                        account,
                        PasswordTokenType.PASSWORD_RESET
                );

        emailService.sendForgotPasswordEmail(
                request.getEmail(),
                token
        );
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        PasswordToken passwordToken =
                passwordTokenService.validateToken(
                        request.getToken(),
                        PasswordTokenType.PASSWORD_RESET
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

        accountRepository.save(account);

        passwordTokenService.markAsUsed(passwordToken);
    }
}