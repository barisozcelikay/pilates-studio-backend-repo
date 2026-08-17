package com.pilatesstudio.member.dto;

import com.pilatesstudio.common.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto extends BaseDto {

    private Long accountId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String status;
    private java.time.LocalDate membershipStartDate;
    private java.time.LocalDate membershipEndDate;
    private String note;
}
