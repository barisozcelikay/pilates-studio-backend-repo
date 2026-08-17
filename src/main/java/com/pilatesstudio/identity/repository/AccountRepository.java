package com.pilatesstudio.identity.repository;

import com.pilatesstudio.identity.entity.Account;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByPhone(String phone);

    Optional<Account> findByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByProfileId(Long profileId);

    List<Account> findAllByProfile_Code(String profileCode);
}
