package com.pilatesstudio.menu.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {

    private Long id;
    private String code;
    private String name;
    private String icon;
    private String route;
    private Long parentId;
    private Integer sortOrder;
    private Boolean active;
    private List<MenuDto> children;
}