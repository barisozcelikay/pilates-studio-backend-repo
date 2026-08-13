package com.pilatesstudio.authentication.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SelectRoleRequest {

    private String role;
    private String roleSelectionToken;

}