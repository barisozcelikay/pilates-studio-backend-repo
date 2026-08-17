package com.pilatesstudio.member.service;

import com.pilatesstudio.common.exception.BusinessException;
import com.pilatesstudio.common.exception.ResourceNotFoundException;
import com.pilatesstudio.identity.entity.Account;
import com.pilatesstudio.identity.entity.Profile;
import com.pilatesstudio.identity.model.AccountStatus;
import com.pilatesstudio.identity.repository.AccountRepository;
import com.pilatesstudio.identity.repository.ProfileRepository;
import com.pilatesstudio.member.dto.MemberDto;
import com.pilatesstudio.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private static final String MEMBER_PROFILE_CODE = "PROFILE_MEMBER";

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final MemberMapper memberMapper;

    public List<MemberDto> findAll() {
        return accountRepository.findAllByProfile_Code(MEMBER_PROFILE_CODE).stream()
                .map(memberMapper::toDto)
                .toList();
    }

    public MemberDto findById(Long id) {
        return memberMapper.toDto(findMember(id));
    }

    @Transactional
    public MemberDto create(MemberDto request) {
        validateUniqueContactDetails(request, null);

        Profile profile = profileRepository.findByCode(MEMBER_PROFILE_CODE)
                .orElseThrow(() -> new ResourceNotFoundException("Üye profili bulunamadı"));

        Account account = new Account();
        apply(request, account);
        account.setProfileId(profile.getId());

        return memberMapper.toDto(accountRepository.save(account));
    }

    @Transactional
    public MemberDto update(MemberDto request) {
        if (request.getId() == null) {
            throw new BusinessException("Güncellenecek üye belirtilmelidir");
        }

        Account account = findMember(request.getId());
        validateUniqueContactDetails(request, account.getId());
        apply(request, account);

        return memberMapper.toDto(accountRepository.save(account));
    }

    @Transactional
    public void delete(Long id) {
        accountRepository.delete(findMember(id));
    }

    private Account findMember(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Üye bulunamadı"));

        if (account.getProfile() == null || !MEMBER_PROFILE_CODE.equals(account.getProfile().getCode())) {
            throw new ResourceNotFoundException("Üye bulunamadı");
        }

        return account;
    }

    private void apply(MemberDto request, Account account) {
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.setPhone(request.getPhone());
        account.setEmail(request.getEmail());
        account.setStatus(request.isActive() ? AccountStatus.ACTIVE : AccountStatus.PASSIVE);
        account.setEmailVerified(false);
    }

    private void validateUniqueContactDetails(MemberDto request, Long currentId) {
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
