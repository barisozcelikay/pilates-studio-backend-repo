package com.pilatesstudio.identity.service;

import com.pilatesstudio.common.exception.BusinessException;
import com.pilatesstudio.common.exception.ResourceNotFoundException;
import com.pilatesstudio.identity.dto.AccountRoleDto;
import com.pilatesstudio.identity.dto.RoleDto;
import com.pilatesstudio.identity.entity.Account;
import com.pilatesstudio.identity.entity.AccountRole;
import com.pilatesstudio.identity.entity.Role;
import com.pilatesstudio.identity.mapper.AccountRoleMapper;
import com.pilatesstudio.identity.mapper.RoleMapper;
import com.pilatesstudio.identity.repository.AccountRoleRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountRoleService {

    private final AccountRoleRepository accountRoleRepository;
    private final AccountRoleMapper accountRoleMapper;
    private final RoleMapper roleMapper;
    private final RoleService roleService;

    public List<AccountRoleDto> findAll() {
        return accountRoleRepository.findAll()
                .stream()
                .map(accountRoleMapper::toDto)
                .toList();
    }

    public List<RoleDto> findRolesByAccountId(Long accountId) {
        return accountRoleRepository.findAllByAccountId(accountId)
                .stream()
                .map(AccountRole::getRole)
                .map(roleMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> findRoleCodesByAccountId(Long accountId) {

        return accountRoleRepository.findAllByAccountId(accountId)
                .stream()
                .map(AccountRole::getRole)
                .map(Role::getCode)
                .toList();
    }

    public AccountRoleDto findById(Long id) {
        return accountRoleRepository.findById(id)
                .map(accountRoleMapper::toDto)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account role not found: " + id
                        ));
    }

    @Transactional
    public AccountRoleDto create(AccountRoleDto dto) {

        if (accountRoleRepository.existsByAccountIdAndRoleId(
                dto.getAccountId(),
                dto.getRoleId())) {

            throw new BusinessException(
                    "Role is already assigned to account"
            );
        }

        AccountRole accountRole = new AccountRole();

        accountRole.setAccountId(dto.getAccountId());
        accountRole.setRoleId(dto.getRoleId());

        AccountRole savedAccountRole =
                accountRoleRepository.save(accountRole);

        return accountRoleMapper.toDto(savedAccountRole);
    }

    @Transactional
    public void delete(
            AccountRoleDto dto
    ) {
        RoleDto role = roleService.findById(dto.getRoleId());

        if ("ROLE_MEMBER".equals(role.getCode())) {
            throw new BusinessException(
                    "ROLE_MEMBER cannot be removed"
            );
        }

        AccountRole accountRole =
                accountRoleRepository
                        .findByAccountIdAndRoleId(
                                dto.getAccountId(),
                                dto.getRoleId()
                        )
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Role is not assigned"
                                )
                        );

        accountRoleRepository.delete(accountRole);
    }

    @Transactional
    public AccountRoleDto assignMemberRoleToNewAccount(Long accountId ) {

        RoleDto role = roleService.findByCode("ROLE_MEMBER");

        if (accountRoleRepository.existsByAccountIdAndRoleId(
                accountId,
                role.getId())) {

            throw new BusinessException(
                    "Role is already assigned to account"
            );
        }

        AccountRole accountRole = new AccountRole();

        accountRole.setAccountId(accountId);
        accountRole.setRoleId(role.getId());

        AccountRole savedAccountRole =
                accountRoleRepository.save(accountRole);

        return accountRoleMapper.toDto(savedAccountRole);
    }


}