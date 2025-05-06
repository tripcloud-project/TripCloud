package com.ssafy.project.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN-001", "유효하지않은 토큰입니다."),
    NOT_FOUND_USER(HttpStatus.UNAUTHORIZED, "ACCOUNT-001", "이메일 또는 비밀번호를 확인하세요."),

    // [Gallery]
    INVALID_UPLOAD_REQUEST(HttpStatus.BAD_REQUEST, "UPLOAD-001", "잘못된 업로드 요청입니다."),
    EMPTY_FILE_UPLOAD(HttpStatus.BAD_REQUEST, "UPLOAD-002", "업로드할 파일이 비어 있습니다."),
    UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "UPLOAD-003", "파일 업로드 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
