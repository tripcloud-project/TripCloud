package com.ssafy.project.domain.member.repository;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.mapper.MemberMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberRepositoryTest {

    private MemberRepository memberRepository;

    @Mock
    private MemberMapper memberMapper;

    @BeforeEach
    void setUp(){
        memberRepository = new MemberRepositoryImpl(memberMapper);
    }

    @Test
    @DisplayName("회원 등록 성공 테스트")
    void insertMember() {
        // given
        String email = "test@example.com";
        String password = "testPassword";
        String name = "홍길동";
        MemberRegisterDto memberRegisterDto = new MemberRegisterDto(email, password, name);

        given(memberMapper.insert(any())).willReturn(1);

        // when
        assertDoesNotThrow(() -> memberRepository.insertMember(memberRegisterDto));

        // then
        verify(memberMapper).insert(any());
    }
}