package com.pilatesstudio.identity.service;

import com.pilatesstudio.common.exception.BusinessException;
import com.pilatesstudio.common.exception.ResourceNotFoundException;
import com.pilatesstudio.identity.dto.ProfileDto;
import com.pilatesstudio.identity.entity.Profile;
import com.pilatesstudio.identity.mapper.ProfileMapper;
import com.pilatesstudio.identity.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final AccountService accountService;

    @Transactional(readOnly = true)
    public List<ProfileDto> findAll() {
        return profileRepository.findAll()
                .stream()
                .map(profileMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProfileDto findById(Long id) {
        return profileRepository.findById(id)
                .map(profileMapper::toDto)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Profile not found")
                );
    }

    public ProfileDto create(ProfileDto request) {

        if (profileRepository.existsByCode(request.getCode())) {
            throw new BusinessException("Profile code already exists");
        }

        Profile profile = new Profile();
        profile.setCode(request.getCode());
        profile.setName(request.getName());
        profile.setActive(true);

        return profileMapper.toDto(
                profileRepository.save(profile)
        );
    }

    public ProfileDto update(ProfileDto request) {

        Profile profile = profileRepository.findById(request.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Profile not found")
                );

        profile.setName(request.getName());
        profile.setActive(request.isActive());

        return profileMapper.toDto(
                profileRepository.save(profile)
        );
    }

    public void delete(Long id) {

        Profile profile = profileRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Profile not found")
                );

        if (accountService.existsByProfileId(id)) {
            throw new BusinessException(
                    "Bu profil bir veya daha fazla kullanıcıya atanmış olduğu için silinemez."
            );
        }

        profileRepository.delete(profile);
    }
}