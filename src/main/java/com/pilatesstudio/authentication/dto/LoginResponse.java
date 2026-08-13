package com.pilatesstudio.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;

    private String tokenType;

    private long expiresIn;

    private boolean roleSelectionRequired;

    private List<String> roles;

    private String roleSelectionToken;


    public static LoginResponse success(
            String accessToken,
            long expiresIn
    ) {
        return new LoginResponse(
                accessToken,
                "Bearer",
                expiresIn,
                false,
                null,
                null
        );
    }


    public static LoginResponse roleSelectionRequired(
            String roleSelectionToken,
            List<String> roles
    ) {
        return new LoginResponse(
                null,
                null,
                0,
                true,
                roles,
                roleSelectionToken
        );
    }
}