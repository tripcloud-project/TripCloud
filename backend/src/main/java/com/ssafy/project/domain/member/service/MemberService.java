package com.ssafy.project.domain.member.service;

<<<<<<< backend/src/main/java/com/ssafy/project/domain/member/service/MemberService.java
import org.springframework.security.core.Authentication;

=======
import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;

import com.ssafy.project.common.response.PageResponse;
>>>>>>> backend/src/main/java/com/ssafy/project/domain/member/service/MemberService.java
import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.dto.response.BadgeListResponseDto;
import com.ssafy.project.domain.member.dto.response.MemberResponseDto;
import com.ssafy.project.domain.member.dto.response.ValidateEmailResponseDto;

public interface MemberService {
    void createMember(MemberRegisterDto memberRegisterDto);
    ValidateEmailResponseDto isEmailDuplicated(String email);
    MemberResponseDto getCurrentMemberInfo(Authentication authentication);
}
