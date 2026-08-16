package com.pilatesstudio.menu.dto;

import com.pilatesstudio.common.dto.BaseDto;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuProfileDto extends BaseDto {

    private Long menuId;
    private Long profileId;
}