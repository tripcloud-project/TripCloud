package com.ssafy.project.domain.member.service;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.repository.MemberRepository;
import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberServiceImpl(memberRepository);
    }


    @Test
    @DisplayName("회원가입 성공 테스트")
    void registerMember() throws DuplicateMemberException {
        // given
        String email = "test@example.com";
        String password = "testPassword";
        String name = "홍길동";
        MemberRegisterDto memberRegisterDto = new MemberRegisterDto(email, password, name);
        given(memberRepository.existsByEmail(email)).willReturn(false);

        // when
        memberService.createMember(memberRegisterDto);

        // then
        verify(memberRepository).insertMember(memberRegisterDto);
    }

}