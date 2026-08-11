package com.pilatesstudio.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SetPasswordRequest {

    @NotBlank
    private String token;

    @NotBlank
    @Size(min = 8, max = 100)
    private String newPassword;
}