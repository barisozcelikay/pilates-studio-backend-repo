package com.pilatesstudio.studioservice.dto;

import com.pilatesstudio.common.dto.BaseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudioServiceDto extends BaseDto {
    @NotBlank @Size(max = 50) private String code;
    @NotBlank @Size(max = 100) private String name;
    @Size(max = 500) private String description;
    private boolean active = true;
}
