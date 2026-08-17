package com.pilatesstudio.lesson.entity;

import com.pilatesstudio.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lesson_instructor", schema = "pilates_studio")
@Getter
@Setter
@NoArgsConstructor
public class LessonInstructor extends BaseEntity {

    @Column(name = "lesson_id", nullable = false)
    private Long lessonId;

    @Column(name = "instructor_id", nullable = false)
    private Long instructorId;
}
