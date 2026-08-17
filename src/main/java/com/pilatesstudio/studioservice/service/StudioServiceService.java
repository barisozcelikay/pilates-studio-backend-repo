package com.pilatesstudio.studioservice.service;
import com.pilatesstudio.common.exception.BusinessException;
import com.pilatesstudio.common.exception.ResourceNotFoundException;
import com.pilatesstudio.studioservice.dto.StudioServiceDto;
import com.pilatesstudio.studioservice.entity.StudioService;
import com.pilatesstudio.studioservice.mapper.StudioServiceMapper;
import com.pilatesstudio.studioservice.repository.StudioServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class StudioServiceService {
 private final StudioServiceRepository repository; private final StudioServiceMapper mapper;
 public List<StudioServiceDto> findAll(){return repository.findAll().stream().map(mapper::toDto).toList();}
 public StudioServiceDto findById(Long id){return mapper.toDto(find(id));}
 @Transactional public StudioServiceDto create(StudioServiceDto dto){if(repository.existsByCode(dto.getCode()))throw new BusinessException("Hizmet kodu zaten kayıtlı");return mapper.toDto(repository.save(mapper.toEntity(dto)));}
 @Transactional public StudioServiceDto update(StudioServiceDto dto){StudioService entity=find(dto.getId());entity.setName(dto.getName());entity.setDescription(dto.getDescription());entity.setActive(dto.isActive());return mapper.toDto(repository.save(entity));}
 @Transactional public void delete(Long id){repository.delete(find(id));}
 private StudioService find(Long id){return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hizmet bulunamadı"));}
}
