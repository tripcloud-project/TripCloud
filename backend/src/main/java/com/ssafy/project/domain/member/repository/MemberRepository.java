package com.ssafy.project.domain.member.repository;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.entity.Member;
import lombok.NonNull;

public interface MemberRepository {
    void insertMember(MemberRegisterDto memberRegisterDto);
    Boolean existsByEmail(@NonNull String email);
    Member selectById(@NonNull Long id); // TODO 테스트 작성 필요
}
