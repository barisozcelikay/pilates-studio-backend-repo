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
    @Transactional public MemberDto create(MemberDto request) { Member member = new Member(); member.setAccountId(request.getAccountId()); apply(request, member); return memberMapper.toDto(memberRepository.save(member)); }
    @Transactional public MemberDto update(MemberDto request) { if (request.getId() == null) throw new BusinessException("Güncellenecek üye belirtilmelidir"); Member member = find(request.getId()); apply(request, member); return memberMapper.toDto(memberRepository.save(member)); }
    @Transactional public void delete(Long id) { memberRepository.delete(find(id)); }
    private Member find(Long id) { return memberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Üye bulunamadı")); }
    private void apply(MemberDto dto, Member member) { member.setMembershipStartDate(dto.getMembershipStartDate()); member.setMembershipEndDate(dto.getMembershipEndDate()); member.setNote(dto.getNote()); }
    @Transactional public void createForAccount(Long accountId) { Member member = new Member(); member.setAccountId(accountId); memberRepository.save(member); }
}
