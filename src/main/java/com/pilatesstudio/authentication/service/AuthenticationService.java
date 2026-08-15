package com.pilatesstudio.authentication.service;

import com.pilatesstudio.authentication.dto.LoginRequest;
import com.pilatesstudio.authentication.dto.LoginResponse;
import com.pilatesstudio.authentication.dto.SelectRoleRequest;
import com.pilatesstudio.common.exception.BusinessException;
import com.pilatesstudio.identity.entity.Account;
import com.pilatesstudio.identity.model.AccountStatus;
import com.pilatesstudio.identity.repository.AccountRepository;
import com.pilatesstudio.authentication.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public LoginResponse login(LoginRequest request) {

        Account account =
                accountRepository.findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new BusinessException("Invalid email")
                        );

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("Account is not active");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                account.getPasswordHash()
        )) {
            throw new BusinessException("Invalid email or password");
        }



        if (account.getProfile() == null) {
            throw new BusinessException(
                    "Account has no assigned profile"
            );
        }

        String profile = account.getProfile().getCode();

        String token = jwtService.generateToken(
                account.getId(),
                profile
        );

        account.setLastLoginAt(LocalDateTime.now());
        accountRepository.save(account);

        return LoginResponse.success(
                token,
                jwtService.getExpiration()
        );
    }
}