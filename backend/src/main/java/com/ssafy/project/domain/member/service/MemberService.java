package com.ssafy.project.domain.member.service;

import java.time.LocalDateTime;

import com.ssafy.project.domain.member.dto.response.StorageResponseDto;
import org.springframework.security.core.Authentication;

import com.ssafy.project.common.response.CursorPageResponse;
import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.dto.request.MemberUpdateRequestDto;
import com.ssafy.project.domain.member.dto.response.BadgeListResponseDto;
import com.ssafy.project.domain.member.dto.response.MemberResponseDto;
import com.ssafy.project.domain.member.dto.response.ValidateEmailResponseDto;

public interface MemberService {
    void createMember(MemberRegisterDto memberRegisterDto);
    ValidateEmailResponseDto isEmailDuplicated(String email);
    MemberResponseDto getCurrentMemberInfo(Authentication authentication);    
    CursorPageResponse<?> getMyActivities(Authentication authentication, LocalDateTime cursor, Integer size);
	BadgeListResponseDto getCurrentMemberBadges(Authentication authentication);
    void putCurrentMemberInfo(MemberUpdateRequestDto requestDto, Authentication authentication);
	void deleteCurrentMember(Authentication authentication);
    StorageResponseDto getStorage(Authentication authentication);
    void updateStorageByMemberId(Long memberId);
}
