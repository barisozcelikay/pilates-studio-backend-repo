package com.pilatesstudio.lesson.dto;

import com.pilatesstudio.common.dto.BaseDto;
import com.pilatesstudio.lesson.model.LessonStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LessonDto extends BaseDto {

    private Long studioId;

    @NotBlank(message = "Ders adı zorunludur")
    @Size(max = 150)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Başlangıç zamanı zorunludur")
    private OffsetDateTime startAt;

    @NotNull(message = "Bitiş zamanı zorunludur")
    private OffsetDateTime endAt;

    @NotNull(message = "Kontenjan zorunludur")
    @Positive(message = "Kontenjan pozitif olmalıdır")
    private Integer capacity;

    private LessonStatus status = LessonStatus.ACTIVE;

    private List<Long> instructorIds = new ArrayList<>();

    private List<String> instructorNames = new ArrayList<>();
}
