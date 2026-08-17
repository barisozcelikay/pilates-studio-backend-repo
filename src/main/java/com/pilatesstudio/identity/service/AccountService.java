package com.pilatesstudio.identity.service;

import com.pilatesstudio.authentication.model.PasswordTokenType;
import com.pilatesstudio.authentication.service.EmailService;
import com.pilatesstudio.authentication.service.PasswordTokenService;
import com.pilatesstudio.common.exception.BusinessException;
import com.pilatesstudio.common.exception.ResourceNotFoundException;
import com.pilatesstudio.identity.dto.AccountDto;
import com.pilatesstudio.identity.entity.Account;
import com.pilatesstudio.identity.mapper.AccountMapper;
import com.pilatesstudio.identity.model.AccountStatus;
import com.pilatesstudio.identity.repository.AccountRepository;
import com.pilatesstudio.identity.repository.ProfileRepository;
import com.pilatesstudio.member.service.MemberService;
import com.pilatesstudio.instructor.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordTokenService passwordTokenService;
    private final EmailService emailService;
    private final ProfileRepository profileRepository;
    private final MemberService memberService;
    private final InstructorService instructorService;

    public List<AccountDto> findAll() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toDto)
                .toList();
    }

    @Transactional
    public AccountDto create(AccountDto accountDto) {

        checkValidationControls(accountDto);
        Account account = setInitialCreateFields(accountDto);
        Account savedAccount = accountRepository.save(account);

        String profileCode = profileRepository.findById(savedAccount.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"))
                .getCode();
        if ("PROFILE_MEMBER".equals(profileCode)) {
            memberService.createForAccount(savedAccount.getId());
        } else if ("PROFILE_INSTRUCTOR".equals(profileCode)) {
            instructorService.createForAccount(savedAccount.getId());
        }

        String token = passwordTokenService.createToken(
                savedAccount,
                PasswordTokenType.INITIAL_PASSWORD
        );

        emailService.sendInitialPasswordEmail(
                savedAccount.getEmail(),
                savedAccount.getFirstName(),
                savedAccount.getLastName(),
                token
        );

        return accountMapper.toDto(savedAccount);
    }



    @Transactional
    public AccountDto update(AccountDto accountDto) {
        Account account = accountRepository.findById(accountDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountDto.getId()));
        if (!account.getProfileId().equals(accountDto.getProfileId())) {
            throw new BusinessException("Profil değişikliği desteklenmiyor");
        }
        account.setFirstName(accountDto.getFirstName());
        account.setLastName(accountDto.getLastName());
        account.setPhone(accountDto.getPhone());
        account.setEmail(accountDto.getEmail());
        return accountMapper.toDto(accountRepository.save(account));
    }

    @Transactional
    public void delete(Long id) {
        accountRepository.delete(accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + id)));
    }


    public AccountDto findById(Long id) {
        return accountRepository.findById(id)
                .map(accountMapper::toDto)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + id
                        ));
    }

    public boolean existsByProfileId(Long id) {
        return accountRepository.existsByProfileId(id);
    }



    private @NonNull Account setInitialCreateFields(AccountDto accountDto) {
        Account account = accountMapper.toEntity(accountDto);
        account.setStatus(AccountStatus.PENDING);
        account.setEmailVerified(false);
        account.setPasswordHash(null);
        return account;
    }

    private void checkValidationControls(AccountDto accountDto) {
        if (accountRepository.existsByPhone(accountDto.getPhone())) {
            throw new BusinessException(
                    "Phone already exists: " + accountDto.getPhone()
            );
        }

        if (accountDto.getEmail() != null
                && accountRepository.existsByEmail(accountDto.getEmail())) {

            throw new BusinessException(
                    "Email already exists: " + accountDto.getEmail()
            );
        }
    }




}
