package com.pilatesstudio.lesson.controller;

import com.pilatesstudio.lesson.dto.LessonDto;
import com.pilatesstudio.lesson.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('PROFILE_ADMIN')")
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public List<LessonDto> findAll() {
        return lessonService.findAll();
    }

    @GetMapping("/{id}")
    public LessonDto findById(@PathVariable Long id) {
        return lessonService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LessonDto create(@Valid @RequestBody LessonDto request) {
        return lessonService.create(request);
    }

    @PutMapping
    public LessonDto update(@Valid @RequestBody LessonDto request) {
        return lessonService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        lessonService.delete(id);
    }
}
