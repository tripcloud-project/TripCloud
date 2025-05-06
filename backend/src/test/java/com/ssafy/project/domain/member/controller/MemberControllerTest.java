package com.ssafy.project.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.service.MemberService;
import com.ssafy.project.util.JWTUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.mockito.BDDMockito.given;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;
    @MockBean
    private JWTUtil jwtUtil;

    private final String API_PATH = "/api/v1/members";

    ResultActions doPost(String endPoint, String body) throws Exception {
        return mockMvc.perform(post(API_PATH + endPoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(body));
    }

    @Test
    @DisplayName("회원가입 API 성공 테스트")
    void insertMember_Success() throws Exception {
        // given
        String email = "test@example.com";
        String password = "testPassword";
        String name = "홍길동";

        MemberRegisterDto memberRegisterDto = new MemberRegisterDto(email, password, name);
        String requestBody = objectMapper.writeValueAsString(memberRegisterDto);

        given(memberService.createMember(memberRegisterDto))
                .willReturn(true);

        // when
        ResultActions perform = doPost("", requestBody);

        // then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andDo(print());
    }
}