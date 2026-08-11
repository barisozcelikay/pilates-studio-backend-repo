package com.pilatesstudio.identity.service;

import com.pilatesstudio.common.exception.ResourceNotFoundException;
import com.pilatesstudio.identity.dto.RoleDto;
import com.pilatesstudio.identity.entity.Role;
import com.pilatesstudio.identity.mapper.RoleMapper;
import com.pilatesstudio.identity.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public List<RoleDto> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toDto)
                .toList();
    }

    public RoleDto findById(Long id) {
        return roleRepository.findById(id)
                .map(roleMapper::toDto)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found: " + id
                        ));
    }

    public RoleDto findByCode(String code) {
        return roleRepository.findByCode(code)
                .map(roleMapper::toDto)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found: " + code
                        ));
    }


    Role findEntityById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found: " + id
                        ));
    }
}