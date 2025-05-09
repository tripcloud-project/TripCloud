package com.ssafy.project.util;

import com.ssafy.project.domain.auth.service.MemberDetails;
import com.ssafy.project.domain.member.entity.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof MemberDetails)) {
            throw new IllegalStateException("인증된 사용자 정보가 없습니다.");
        }
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        return memberDetails.member();
    }

    public static String getCurrentMemberEmail() {
        return getCurrentMember().getEmail();
    }

    public static Long getCurrentMemberId() {
        return getCurrentMember().getMemberId();
    }
}
