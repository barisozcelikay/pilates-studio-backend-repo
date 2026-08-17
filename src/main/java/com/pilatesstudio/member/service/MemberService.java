package com.pilatesstudio.member.service;

import com.pilatesstudio.common.exception.BusinessException;
import com.pilatesstudio.common.exception.ResourceNotFoundException;
import com.pilatesstudio.member.dto.MemberDto;
import com.pilatesstudio.member.entity.Member;
import com.pilatesstudio.member.mapper.MemberMapper;
import com.pilatesstudio.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    public List<MemberDto> findAll() { return memberRepository.findAll().stream().map(memberMapper::toDto).toList(); }
    public MemberDto findById(Long id) { return memberMapper.toDto(find(id)); }
    @Transactional public MemberDto create(MemberDto request) { validate(request, null); Member member = new Member(); apply(request, member); return memberMapper.toDto(memberRepository.save(member)); }
    @Transactional public MemberDto update(MemberDto request) { if (request.getId() == null) throw new BusinessException("Güncellenecek üye belirtilmelidir"); Member member = find(request.getId()); validate(request, member.getId()); apply(request, member); return memberMapper.toDto(memberRepository.save(member)); }
    @Transactional public void delete(Long id) { memberRepository.delete(find(id)); }
    private Member find(Long id) { return memberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Üye bulunamadı")); }
    private void apply(MemberDto dto, Member member) { member.setFirstName(dto.getFirstName()); member.setLastName(dto.getLastName()); member.setPhone(dto.getPhone()); member.setEmail(dto.getEmail()); member.setActive(dto.isActive()); }
    private void validate(MemberDto dto, Long id) {
        memberRepository.findByPhone(dto.getPhone()).filter(value -> !value.getId().equals(id)).ifPresent(value -> { throw new BusinessException("Bu telefon numarası zaten kayıtlı"); });
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) memberRepository.findByEmail(dto.getEmail()).filter(value -> !value.getId().equals(id)).ifPresent(value -> { throw new BusinessException("Bu e-posta adresi zaten kayıtlı"); });
    }
}
