package com.pilatesstudio.instructor.dto;

import com.pilatesstudio.common.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstructorDto extends BaseDto {

    private Long accountId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String status;
    private String specialty;
    private String biography;
}
