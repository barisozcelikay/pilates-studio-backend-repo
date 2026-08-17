package com.pilatesstudio.instructor.service;
import com.pilatesstudio.common.exception.*;
import com.pilatesstudio.instructor.dto.InstructorDto;
import com.pilatesstudio.instructor.entity.Instructor;
import com.pilatesstudio.instructor.mapper.InstructorMapper;
import com.pilatesstudio.instructor.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor @Transactional(readOnly=true)
public class InstructorService {
 private final InstructorRepository instructorRepository; private final InstructorMapper instructorMapper;
 public List<InstructorDto> findAll(){return instructorRepository.findAll().stream().map(instructorMapper::toDto).toList();}
 public InstructorDto findById(Long id){return instructorMapper.toDto(find(id));}
 @Transactional public InstructorDto create(InstructorDto dto){validate(dto,null); Instructor instructor=new Instructor(); apply(dto,instructor); return instructorMapper.toDto(instructorRepository.save(instructor));}
 @Transactional public InstructorDto update(InstructorDto dto){if(dto.getId()==null)throw new BusinessException("Güncellenecek eğitmen belirtilmelidir"); Instructor instructor=find(dto.getId());validate(dto,instructor.getId());apply(dto,instructor);return instructorMapper.toDto(instructorRepository.save(instructor));}
 @Transactional public void delete(Long id){instructorRepository.delete(find(id));}
 private Instructor find(Long id){return instructorRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Eğitmen bulunamadı"));}
 private void apply(InstructorDto dto,Instructor value){value.setFirstName(dto.getFirstName());value.setLastName(dto.getLastName());value.setPhone(dto.getPhone());value.setEmail(dto.getEmail());value.setActive(dto.isActive());}
 private void validate(InstructorDto dto,Long id){instructorRepository.findByPhone(dto.getPhone()).filter(value->!value.getId().equals(id)).ifPresent(value->{throw new BusinessException("Bu telefon numarası zaten kayıtlı");});if(dto.getEmail()!=null&&!dto.getEmail().isBlank())instructorRepository.findByEmail(dto.getEmail()).filter(value->!value.getId().equals(id)).ifPresent(value->{throw new BusinessException("Bu e-posta adresi zaten kayıtlı");});}
}
