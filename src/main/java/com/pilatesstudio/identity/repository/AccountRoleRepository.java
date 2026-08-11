package com.pilatesstudio.identity.repository;

import com.pilatesstudio.identity.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {

    boolean existsByAccountIdAndRoleId(Long accountId, Long roleId);

    List<AccountRole> findAllByAccountId(Long accountId);
}