package com.pilatesstudio.menu.dto;

import com.pilatesstudio.common.dto.BaseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto extends BaseDto {

    private String code;
    private String name;
    private String icon;
    private String route;
    private Long parentId;
    private Integer sortOrder;
    private Boolean active;
    private List<MenuDto> children;
    private List<Long> profileIds;
}