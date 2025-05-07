package com.ssafy.project.domain.member.repository;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.entity.Member;
import com.ssafy.project.domain.member.mapper.MemberMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberMapper memberMapper;

    @Override
    public void insertMember(MemberRegisterDto memberRegisterDto) {
        memberMapper.insert(memberRegisterDto);
    }

    @Override
    public Boolean existsByEmail(@NonNull String email) {
        return memberMapper.existsByEmail(email);
    }

    @Override
    public Member selectById(@NonNull Long id) {
        return memberMapper.selectById(id);
    }
}
