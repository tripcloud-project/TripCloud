package com.ssafy.project.domain.auth.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.project.domain.auth.dto.request.LoginRequestDto;
import com.ssafy.project.domain.auth.dto.response.LoginResponseDto;
import com.ssafy.project.domain.auth.service.AuthService;
import com.ssafy.project.util.JWTUtil;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private AuthService authService;
    @MockBean
    private JWTUtil jwtUtil;
    
    private final String API_PATH = "/api/v1/auth";

    ResultActions doPost(String endPoint, String body) throws Exception {
        return mockMvc.perform(post(API_PATH + endPoint)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(body));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginTest_Success() throws Exception {
        // given
        String email = "test@example.com";
        String password = "testPassword";
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);
        String requestBody = objectMapper.writeValueAsString(loginRequestDto);

        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken);
        
        given(authService.login(loginRequestDto)).willReturn(loginResponseDto);

        // when
        ResultActions perform = doPost("/login", requestBody);
        
        // then
        perform.andExpect(status().isOk())
        			.andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
        			.andExpect(cookie().value("refresh_token", refreshToken))
        			.andDo(print());
    }
}