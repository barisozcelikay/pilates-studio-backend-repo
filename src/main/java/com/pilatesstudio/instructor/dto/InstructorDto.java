package com.pilatesstudio.instructor.dto;

import com.pilatesstudio.common.dto.BaseDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstructorDto extends BaseDto {

    @NotBlank(message = "Ad zorunludur")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Soyad zorunludur")
    @Size(max = 100)
    private String lastName;

    @NotBlank(message = "Telefon zorunludur")
    @Size(max = 20)
    private String phone;

    @Email(message = "E-posta adresi geçerli olmalıdır")
    @Size(max = 255)
    private String email;

    private boolean active = true;
}
