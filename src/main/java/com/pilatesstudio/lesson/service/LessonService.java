package com.pilatesstudio.lesson.service;

import com.pilatesstudio.common.exception.BusinessException;
import com.pilatesstudio.common.exception.ResourceNotFoundException;
import com.pilatesstudio.identity.entity.Account;
import com.pilatesstudio.identity.repository.AccountRepository;
import com.pilatesstudio.lesson.dto.LessonDto;
import com.pilatesstudio.lesson.entity.Lesson;
import com.pilatesstudio.lesson.entity.LessonInstructor;
import com.pilatesstudio.lesson.mapper.LessonMapper;
import com.pilatesstudio.lesson.repository.LessonInstructorRepository;
import com.pilatesstudio.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LessonService {

    private static final String INSTRUCTOR_PROFILE_CODE = "PROFILE_INSTRUCTOR";

    private final LessonRepository lessonRepository;
    private final LessonInstructorRepository lessonInstructorRepository;
    private final AccountRepository accountRepository;
    private final LessonMapper lessonMapper;

    public List<LessonDto> findAll() {
        return lessonRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public LessonDto findById(Long id) {
        return toDto(findLesson(id));
    }

    @Transactional
    public LessonDto create(LessonDto request) {
        validateSchedule(request);
        List<Long> instructorIds = validateInstructors(request.getInstructorIds());

        Lesson lesson = lessonMapper.toEntity(request);
        Lesson savedLesson = lessonRepository.save(lesson);
        replaceInstructors(savedLesson.getId(), instructorIds);

        return toDto(savedLesson);
    }

    @Transactional
    public LessonDto update(LessonDto request) {
        if (request.getId() == null) {
            throw new BusinessException("Güncellenecek ders belirtilmelidir");
        }

        validateSchedule(request);
        List<Long> instructorIds = validateInstructors(request.getInstructorIds());
        Lesson lesson = findLesson(request.getId());

        lesson.setStudioId(request.getStudioId());
        lesson.setName(request.getName());
        lesson.setDescription(request.getDescription());
        lesson.setStartAt(request.getStartAt());
        lesson.setEndAt(request.getEndAt());
        lesson.setCapacity(request.getCapacity());
        lesson.setStatus(request.getStatus());

        Lesson savedLesson = lessonRepository.save(lesson);
        lessonInstructorRepository.deleteAllByLessonId(savedLesson.getId());
        replaceInstructors(savedLesson.getId(), instructorIds);

        return toDto(savedLesson);
    }

    @Transactional
    public void delete(Long id) {
        Lesson lesson = findLesson(id);
        lessonInstructorRepository.deleteAllByLessonId(id);
        lessonRepository.delete(lesson);
    }

    private Lesson findLesson(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ders bulunamadı"));
    }

    private LessonDto toDto(Lesson lesson) {
        LessonDto dto = lessonMapper.toDto(lesson);
        List<Long> instructorIds = lessonInstructorRepository.findAllByLessonId(lesson.getId()).stream()
                .map(LessonInstructor::getInstructorId)
                .toList();

        dto.setInstructorIds(instructorIds);
        dto.setInstructorNames(accountRepository.findAllById(instructorIds).stream()
                .map(account -> account.getFirstName() + " " + account.getLastName())
                .toList());
        return dto;
    }

    private void validateSchedule(LessonDto request) {
        if (!request.getEndAt().isAfter(request.getStartAt())) {
            throw new BusinessException("Bitiş zamanı başlangıç zamanından sonra olmalıdır");
        }
    }

    private List<Long> validateInstructors(List<Long> instructorIds) {
        List<Long> ids = instructorIds == null ? List.of() : instructorIds.stream().distinct().toList();
        Map<Long, Account> instructorsById = accountRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Account::getId, Function.identity()));

        boolean invalidInstructor = ids.stream().anyMatch(id -> {
            Account account = instructorsById.get(id);
            return account == null
                    || account.getProfile() == null
                    || !INSTRUCTOR_PROFILE_CODE.equals(account.getProfile().getCode());
        });

        if (invalidInstructor) {
            throw new BusinessException("Seçilen eğitmenlerden biri geçersiz");
        }

        return ids;
    }

    private void replaceInstructors(Long lessonId, List<Long> instructorIds) {
        List<LessonInstructor> relations = instructorIds.stream()
                .map(instructorId -> {
                    LessonInstructor relation = new LessonInstructor();
                    relation.setLessonId(lessonId);
                    relation.setInstructorId(instructorId);
                    return relation;
                })
                .toList();

        lessonInstructorRepository.saveAll(relations);
    }
}
