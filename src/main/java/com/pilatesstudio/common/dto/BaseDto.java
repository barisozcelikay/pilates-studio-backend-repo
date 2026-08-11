package com.pilatesstudio.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseDto {

    private Long id;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}