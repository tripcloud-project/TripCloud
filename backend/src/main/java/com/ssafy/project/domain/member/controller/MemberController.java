package com.ssafy.project.domain.member.controller;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;

import static com.ssafy.project.common.response.ApiResponse.createSuccess;
import static com.ssafy.project.common.response.ApiResponse.createSuccessWithNoContent;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping
    private ResponseEntity<?> registerMember(@RequestBody MemberRegisterDto memberRegisterDto) {
        memberService.createMember(memberRegisterDto);
        return ResponseEntity.status(201)
                .body(createSuccessWithNoContent());
    }

    // 이메일 중복 검사
    @GetMapping("/checkEmail")
    private ResponseEntity<?> checkEmail(@RequestParam String email) {
        return ResponseEntity.status(200)
                .body(createSuccess(memberService.isEmailDuplicated(email)));
    }

    // 마이페이지 조회
    @GetMapping("/me")
    private ResponseEntity<?> getMyInformation(Authentication authentication) {
        return ResponseEntity.status(200)
                .body(createSuccess(memberService.getCurrentMemberInfo(authentication)));
    }
}
