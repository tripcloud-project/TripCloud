package com.ssafy.project.domain.member.repository;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import lombok.NonNull;

public interface MemberRepository {
    void insertMember(MemberRegisterDto memberRegisterDto);

    Boolean existsByEmail(@NonNull String email);
}
