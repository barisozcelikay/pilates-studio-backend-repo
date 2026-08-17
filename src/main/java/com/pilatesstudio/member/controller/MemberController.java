package com.pilatesstudio.member.controller;

import com.pilatesstudio.member.dto.MemberDto;
import com.pilatesstudio.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('PROFILE_ADMIN')")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public List<MemberDto> findAll() {
        return memberService.findAll();
    }

    @GetMapping("/{id}")
    public MemberDto findById(@PathVariable Long id) {
        return memberService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberDto create(@Valid @RequestBody MemberDto request) {
        return memberService.create(request);
    }

    @PutMapping
    public MemberDto update(@Valid @RequestBody MemberDto request) {
        return memberService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        memberService.delete(id);
    }
}
