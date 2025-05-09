package com.ssafy.project.domain.member.controller;

import static com.ssafy.project.common.response.ApiResponse.createSuccess;
import static com.ssafy.project.common.response.ApiResponse.createSuccessWithNoContent;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.dto.request.MemberUpdateRequestDto;
import com.ssafy.project.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

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

    // 유저 활동 내역 조회
    @GetMapping("/me/activities")
    private ResponseEntity<?> getMyActivities(Authentication authentication,
    		@RequestParam LocalDateTime cursor, @RequestParam Integer size){
    	return ResponseEntity.status(200)
    			.body(createSuccess(memberService.getMyActivities(authentication, cursor, size)));
    }

    // 보유 칭호 조회
    @GetMapping("/me/badges")
    private ResponseEntity<?> getMyBadges(Authentication authentication){
    	return ResponseEntity.status(200)
    			.body(createSuccess(memberService.getCurrentMemberBadges(authentication)));
    }

    // 개인 정보 수정
    @PutMapping("/me")
    private ResponseEntity<?> putMyInformation(@RequestBody MemberUpdateRequestDto requestDto,
    		Authentication authentication){
    	memberService.putCurrentMemberInfo(requestDto, authentication);
    	return ResponseEntity.status(200)
    			.body(createSuccessWithNoContent());
    }
}
