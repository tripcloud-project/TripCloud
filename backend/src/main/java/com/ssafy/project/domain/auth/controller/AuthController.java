package com.ssafy.project.domain.auth.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.project.common.response.ApiResponse;
import com.ssafy.project.domain.auth.dto.request.LoginRequestDto;
import com.ssafy.project.domain.auth.dto.response.LoginResponseDto;
import com.ssafy.project.domain.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController // 이 클래스는 REST Controller 임을 명시.
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth") // URI 지정.
public class AuthController {

    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto loginResponseDto = authService.login(loginRequest);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponseDto.getAccessToken());

        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", loginResponseDto.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        httpHeaders.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return ResponseEntity.status(200)
                .headers(httpHeaders)
                .body(ApiResponse.createSuccessWithNoContent());
    }




    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestHeader("Authorization") String authorization,
            @CookieValue("refresh_token") String refreshToken
    ) {

        //authService.logout(authorization, refreshToken);

        // AccessToken은 HTTP Header에 담아 전송.
        HttpHeaders httpHeaders = new HttpHeaders();

        // RefreshToken은 Cookie에 담는다.
        // Http Only 로 반드시 전송해주어야한다. --> Client에서 JS로 접근을 못하게 막는다. (XSS 공격 방지)
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        httpHeaders.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .build();
    }


}
