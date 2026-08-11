package com.pilatesstudio.identity.repository;

import com.pilatesstudio.identity.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {

    boolean existsByAccountIdAndRoleId(Long accountId, Long roleId);

    List<AccountRole> findAllByAccountId(Long accountId);

    Optional<AccountRole> findByAccountIdAndRoleId(
            Long accountId,
            Long roleId
    );}