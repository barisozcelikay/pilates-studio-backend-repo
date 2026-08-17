package com.pilatesstudio.lesson.entity;

import com.pilatesstudio.common.entity.BaseEntity;
import com.pilatesstudio.lesson.model.LessonStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "lesson", schema = "pilates_studio")
@Getter
@Setter
@NoArgsConstructor
public class Lesson extends BaseEntity {

    @Column(name = "studio_id")
    private Long studioId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "start_at", nullable = false)
    private OffsetDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private OffsetDateTime endAt;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private LessonStatus status = LessonStatus.ACTIVE;
}
