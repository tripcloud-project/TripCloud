package com.ssafy.project.domain.member.service;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.dto.response.MemberResponseDto;
import com.ssafy.project.domain.member.dto.response.ValidateEmailResponseDto;
import org.springframework.security.core.Authentication;

public interface MemberService {
    void createMember(MemberRegisterDto memberRegisterDto);
    ValidateEmailResponseDto isEmailDuplicated(String email);
    MemberResponseDto getCurrentMemberInfo(Authentication authentication);
}
