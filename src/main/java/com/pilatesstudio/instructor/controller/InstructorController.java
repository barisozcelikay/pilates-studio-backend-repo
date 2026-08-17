package com.pilatesstudio.instructor.controller;

import com.pilatesstudio.instructor.dto.InstructorDto;
import com.pilatesstudio.instructor.service.InstructorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('PROFILE_ADMIN')")
public class InstructorController {

    private final InstructorService instructorService;

    @GetMapping
    public List<InstructorDto> findAll() {
        return instructorService.findAll();
    }

    @GetMapping("/{id}")
    public InstructorDto findById(@PathVariable Long id) {
        return instructorService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstructorDto create(@Valid @RequestBody InstructorDto request) {
        return instructorService.create(request);
    }

    @PutMapping
    public InstructorDto update(@Valid @RequestBody InstructorDto request) {
        return instructorService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        instructorService.delete(id);
    }
}
