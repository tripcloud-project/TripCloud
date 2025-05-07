package com.ssafy.project.domain.member.service;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;

public interface MemberService {
    void createMember(MemberRegisterDto memberRegisterDto);
}
