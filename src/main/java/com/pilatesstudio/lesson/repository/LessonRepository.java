package com.pilatesstudio.lesson.repository;

import com.pilatesstudio.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
