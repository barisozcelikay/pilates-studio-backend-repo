package com.pilatesstudio.lesson.repository;

import com.pilatesstudio.lesson.entity.LessonInstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonInstructorRepository extends JpaRepository<LessonInstructor, Long> {

    List<LessonInstructor> findAllByLessonId(Long lessonId);

    void deleteAllByLessonId(Long lessonId);
}
