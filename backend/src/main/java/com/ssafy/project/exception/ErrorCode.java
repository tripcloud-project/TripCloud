package com.ssafy.project.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN-001", "유효하지않은 토큰입니다."),
    NOT_FOUND_USER(HttpStatus.UNAUTHORIZED, "ACCOUNT-001", "이메일 또는 비밀번호를 확인하세요."),
    DUPLICATE_MEMBER(HttpStatus.CONFLICT, "ACCOUNT-002", "이미 존재하는 회원입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
