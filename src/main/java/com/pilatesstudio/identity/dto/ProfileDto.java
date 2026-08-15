package com.pilatesstudio.identity.dto;

import com.pilatesstudio.common.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDto extends BaseDto {

    private Long id;

    private String code;

    private String name;

    private boolean active;
}