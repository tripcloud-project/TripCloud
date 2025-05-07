package com.ssafy.project.domain.member.controller;

import com.ssafy.project.domain.auth.service.MemberDetails;
import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;

import static com.ssafy.project.common.response.ApiResponse.createSuccessWithNoContent;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

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

    @GetMapping("/nothing")
    private ResponseEntity<?> doNothing() {
        MemberDetails member = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("member = " + member.member());
        return ResponseEntity.noContent().build();
    }
}
