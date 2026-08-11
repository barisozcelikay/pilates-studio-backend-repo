package com.pilatesstudio.authentication.service;

import com.pilatesstudio.authentication.dto.LoginRequest;
import com.pilatesstudio.authentication.dto.LoginResponse;
import com.pilatesstudio.authentication.jwt.JwtService;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        Account account = accountService.findEntityByPhone(request.getPhone());

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
}