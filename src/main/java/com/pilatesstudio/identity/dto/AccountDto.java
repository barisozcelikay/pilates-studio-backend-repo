package com.pilatesstudio.identity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pilatesstudio.common.dto.BaseDto;
import com.pilatesstudio.identity.model.AccountStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class AccountDto extends BaseDto {

    @NotBlank(message = "Phone is required")
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    private AccountStatus status = AccountStatus.PENDING;

    private boolean emailVerified = false;

    private Long profileId;

    private String profileCode;

    private String profileName;

    private LocalDateTime lastLoginAt;
}