package com.pilatesstudio.instructor.repository;
import com.pilatesstudio.instructor.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

}
