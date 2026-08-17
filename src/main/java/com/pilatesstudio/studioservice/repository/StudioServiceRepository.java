package com.pilatesstudio.studioservice.repository;
import com.pilatesstudio.studioservice.entity.StudioService;
import org.springframework.data.jpa.repository.JpaRepository;
public interface StudioServiceRepository extends JpaRepository<StudioService, Long> { boolean existsByCode(String code); }
