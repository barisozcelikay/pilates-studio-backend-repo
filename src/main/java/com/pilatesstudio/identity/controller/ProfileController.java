package com.pilatesstudio.identity.controller;

import com.pilatesstudio.identity.dto.ProfileDto;
import com.pilatesstudio.identity.entity.Profile;
import com.pilatesstudio.identity.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('PROFILE_ADMIN')")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public List<ProfileDto> findAll() {
        return profileService.findAll();
    }

    @GetMapping("/{id}")
    public ProfileDto findById(@PathVariable Long id) {
        return profileService.findById(id);
    }

    @PostMapping
    public ProfileDto create(@RequestBody ProfileDto request) {
        return
                profileService.create(
                        request
                );

    }

    @PutMapping
    public ProfileDto update(
            @RequestBody ProfileDto request
    ) {

               return profileService.update(
                        request
                );

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        profileService.delete(id);
    }


}