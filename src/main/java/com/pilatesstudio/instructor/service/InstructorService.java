package com.pilatesstudio.instructor.service;

import com.pilatesstudio.common.exception.BusinessException;
import com.pilatesstudio.common.exception.ResourceNotFoundException;
import com.pilatesstudio.identity.entity.Account;
import com.pilatesstudio.identity.entity.Profile;
import com.pilatesstudio.identity.model.AccountStatus;
import com.pilatesstudio.identity.repository.AccountRepository;
import com.pilatesstudio.identity.repository.ProfileRepository;
import com.pilatesstudio.instructor.dto.InstructorDto;
import com.pilatesstudio.instructor.mapper.InstructorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InstructorService {

    private static final String INSTRUCTOR_PROFILE_CODE = "PROFILE_INSTRUCTOR";

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final InstructorMapper instructorMapper;

    public List<InstructorDto> findAll() {
        return accountRepository.findAllByProfile_Code(INSTRUCTOR_PROFILE_CODE).stream()
                .map(instructorMapper::toDto)
                .toList();
    }

    public InstructorDto findById(Long id) {
        return instructorMapper.toDto(findInstructor(id));
    }

    @Transactional
    public InstructorDto create(InstructorDto request) {
        validateUniqueContactDetails(request, null);
        Profile profile = profileRepository.findByCode(INSTRUCTOR_PROFILE_CODE)
                .orElseThrow(() -> new ResourceNotFoundException("Eğitmen profili bulunamadı"));

        Account instructor = new Account();
        apply(request, instructor);
        instructor.setProfileId(profile.getId());

        return instructorMapper.toDto(accountRepository.save(instructor));
    }

    @Transactional
    public InstructorDto update(InstructorDto request) {
        if (request.getId() == null) {
            throw new BusinessException("Güncellenecek eğitmen belirtilmelidir");
        }

        Account instructor = findInstructor(request.getId());
        validateUniqueContactDetails(request, instructor.getId());
        apply(request, instructor);

        return instructorMapper.toDto(accountRepository.save(instructor));
    }

    @Transactional
    public void delete(Long id) {
        accountRepository.delete(findInstructor(id));
    }

    private Account findInstructor(Long id) {
        Account instructor = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Eğitmen bulunamadı"));

        if (instructor.getProfile() == null || !INSTRUCTOR_PROFILE_CODE.equals(instructor.getProfile().getCode())) {
            throw new ResourceNotFoundException("Eğitmen bulunamadı");
        }

        return instructor;
    }

    private void apply(InstructorDto request, Account instructor) {
        instructor.setFirstName(request.getFirstName());
        instructor.setLastName(request.getLastName());
        instructor.setPhone(request.getPhone());
        instructor.setEmail(request.getEmail());
        instructor.setStatus(request.isActive() ? AccountStatus.ACTIVE : AccountStatus.PASSIVE);
        instructor.setEmailVerified(false);
    }

    private void validateUniqueContactDetails(InstructorDto request, Long currentId) {
        accountRepository.findByPhone(request.getPhone())
                .filter(account -> !account.getId().equals(currentId))
                .ifPresent(account -> { throw new BusinessException("Bu telefon numarası zaten kayıtlı"); });

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            accountRepository.findByEmail(request.getEmail())
                    .filter(account -> !account.getId().equals(currentId))
                    .ifPresent(account -> { throw new BusinessException("Bu e-posta adresi zaten kayıtlı"); });
        }
    }
}
