package com.pilatesstudio.identity.repository;

import com.pilatesstudio.identity.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByCode(String code);

    boolean existsByCode(String code);
}