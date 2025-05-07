package com.ssafy.project.domain.member.controller;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;

import static com.ssafy.project.common.response.ApiResponse.createSuccess;
import static com.ssafy.project.common.response.ApiResponse.createSuccessWithNoContent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    private ResponseEntity<?> registerMember(@RequestBody MemberRegisterDto memberRegisterDto) {
        memberService.createMember(memberRegisterDto);
        return ResponseEntity.status(201)
                .body(createSuccessWithNoContent());
    }

    @GetMapping("/checkEmail")
    private ResponseEntity<?> checkEmail(@RequestParam String email) {
        return ResponseEntity.status(200)
                .body(createSuccess(memberService.isEmailDuplicated(email)));
    }
}
