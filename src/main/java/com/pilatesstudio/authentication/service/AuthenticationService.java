package com.pilatesstudio.authentication.service;

import com.pilatesstudio.authentication.dto.*;
import com.pilatesstudio.authentication.entity.PasswordToken;
import com.pilatesstudio.authentication.jwt.JwtService;
import com.pilatesstudio.authentication.model.PasswordTokenType;
import com.pilatesstudio.common.exception.BusinessException;
import com.pilatesstudio.common.exception.ResourceNotFoundException;
import com.pilatesstudio.identity.dto.AccountDto;
import com.pilatesstudio.identity.dto.RoleDto;
import com.pilatesstudio.identity.entity.Account;
import com.pilatesstudio.identity.model.AccountStatus;
import com.pilatesstudio.identity.repository.AccountRepository;
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

    private final AccountRepository accountRepository;
    private final AccountRoleService accountRoleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        Account account =
                accountRepository.findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Invalid email"
                                )
                        );

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException(
                    "Account is not active"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                account.getPasswordHash()
        )) {
            throw new BusinessException(
                    "Invalid email or password"
            );
        }

        List<String> roles = accountRoleService
                .findRolesByAccountId(account.getId())
                .stream()
                .map(RoleDto::getCode)
                .toList();

        if (roles.isEmpty()) {
            throw new BusinessException(
                    "Account has no assigned role"
            );
        }

        // Tek rol → direkt giriş
        if (roles.size() == 1) {

            String token = jwtService.generateToken(
                    account.getId(),
                    roles
            );

            System.out.println("ACCOUNT ID: " + account.getId());
            System.out.println("ROLES: " + roles);

            return LoginResponse.success(
                    token,
                    jwtService.getExpiration()
            );


        }

        String roleSelectionToken =
                jwtService.generateRoleSelectionToken(
                        account.getId(),
                        roles
                );

        System.out.println("ACCOUNT ID: " + account.getId());
        System.out.println("ROLES: " + roles);

        return LoginResponse.roleSelectionRequired(
                roleSelectionToken,
                roles
        );
    }

    @Transactional(readOnly = true)
    public LoginResponse selectRole(
            SelectRoleRequest request
    ) {

        Long accountId =
                jwtService.getAccountIdFromRoleSelectionToken(
                        request.getRoleSelectionToken()
                );

        List<String> roles =
                accountRoleService
                        .findRolesByAccountId(accountId)
                        .stream()
                        .map(RoleDto::getCode)
                        .toList();

        if (!roles.contains(request.getRole())) {
            throw new BusinessException(
                    "Selected role is not assigned to account"
            );
        }

        String token =
                jwtService.generateToken(
                        accountId,
                        List.of(request.getRole())
                );

        return LoginResponse.success(
                token,
                jwtService.getExpiration()
        );
    }
}