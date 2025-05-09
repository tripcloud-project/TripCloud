package com.ssafy.project.domain.member.service;

import org.springframework.security.core.Authentication;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.dto.response.BadgeListResponseDto;
import com.ssafy.project.domain.member.dto.response.MemberResponseDto;
import com.ssafy.project.domain.member.dto.response.ValidateEmailResponseDto;

public interface MemberService {
    void createMember(MemberRegisterDto memberRegisterDto);
    ValidateEmailResponseDto isEmailDuplicated(String email);
    MemberResponseDto getCurrentMemberInfo(Authentication authentication);
    BadgeListResponseDto getCurrentMemberBadges(Authentication authentication);
}
