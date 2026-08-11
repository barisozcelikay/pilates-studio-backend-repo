package com.pilatesstudio.authentication.service;

import com.pilatesstudio.authentication.dto.ForgotPasswordRequest;
import com.pilatesstudio.authentication.dto.LoginRequest;
import com.pilatesstudio.authentication.dto.LoginResponse;
import com.pilatesstudio.authentication.dto.ResetPasswordRequest;
import com.pilatesstudio.authentication.dto.SetPasswordRequest;
import com.pilatesstudio.authentication.entity.PasswordToken;
import com.pilatesstudio.authentication.jwt.JwtService;
import com.pilatesstudio.authentication.model.PasswordTokenType;
import com.pilatesstudio.common.exception.BusinessException;
import com.pilatesstudio.identity.dto.RoleDto;
import com.pilatesstudio.identity.entity.Account;
import com.pilatesstudio.identity.model.AccountStatus;
import com.pilatesstudio.identity.service.AccountRoleService;
import com.pilatesstudio.identity.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountService accountService;
    private final AccountRoleService accountRoleService;
    private final PasswordTokenService passwordTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        Account account =
                accountService.findEntityByEmail(request.getEmail());

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("Account is not active");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                account.getPasswordHash()
        )) {
            throw new BusinessException("Invalid phone or password");
        }

        List<String> roles = accountRoleService
                .findRolesByAccountId(account.getId())
                .stream()
                .map(RoleDto::getCode)
                .toList();

        String token = jwtService.generateToken(
                account.getId(),
                roles
        );

        return new LoginResponse(
                token,
                "Bearer",
                jwtService.getExpiration()
        );
    }

    @Transactional
    public void setPassword(SetPasswordRequest request) {

        PasswordToken passwordToken =
                passwordTokenService.validateToken(
                        request.getToken(),
                        PasswordTokenType.INITIAL_PASSWORD
                );

        Account account = passwordToken.getAccount();

        if (account.getStatus() != AccountStatus.PENDING) {
            throw new BusinessException("Account is not pending");
        }

        account.setPasswordHash(
                passwordEncoder.encode(request.getNewPassword())
        );

        account.setStatus(AccountStatus.ACTIVE);
        account.setEmailVerified(true);

        passwordTokenService.markAsUsed(passwordToken);
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {

        Account account =
                accountService.findEntityByEmail(request.getEmail());

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("Account is not active");
        }

        String token = passwordTokenService.createToken(
                account,
                PasswordTokenType.PASSWORD_RESET
        );

        // TODO: EmailService eklendiğinde burada email gönderilecek.
        System.out.println("PASSWORD RESET TOKEN: " + token);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        PasswordToken passwordToken =
                passwordTokenService.validateToken(
                        request.getToken(),
                        PasswordTokenType.PASSWORD_RESET
                );

        Account account = passwordToken.getAccount();

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("Account is not active");
        }

        account.setPasswordHash(
                passwordEncoder.encode(request.getNewPassword())
        );

        passwordTokenService.markAsUsed(passwordToken);
    }
}