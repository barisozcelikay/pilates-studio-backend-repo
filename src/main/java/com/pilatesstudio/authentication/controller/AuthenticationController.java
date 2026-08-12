package com.pilatesstudio.authentication.controller;

import com.pilatesstudio.authentication.dto.ForgotPasswordRequest;
import com.pilatesstudio.authentication.dto.LoginRequest;
import com.pilatesstudio.authentication.dto.LoginResponse;
import com.pilatesstudio.authentication.dto.SetPasswordRequest;
import com.pilatesstudio.authentication.service.AuthenticationService;
import com.pilatesstudio.authentication.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(
                authenticationService.login(request)
        );
    }

    @PostMapping("/set-password")
    public ResponseEntity<Void> setPassword(
            @Valid @RequestBody SetPasswordRequest request
    ) {
        authenticationService.setPassword(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        authenticationService.forgotPassword(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody SetPasswordRequest request
    ) {
        authenticationService.setPassword(request);
        return ResponseEntity.noContent().build();
    }
}