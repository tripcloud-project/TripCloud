package com.ssafy.project.domain.member.controller;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ssafy.project.common.response.ApiResponse.createSuccessWithNoContent;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    private ResponseEntity<?> registerMember(@RequestBody MemberRegisterDto memberRegisterDto) throws DuplicateMemberException {
        memberService.createMember(memberRegisterDto);
        return ResponseEntity.status(201)
                .body(createSuccessWithNoContent());
    }
}
