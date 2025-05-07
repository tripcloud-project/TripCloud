package com.ssafy.project.exception;


import com.ssafy.project.common.response.ApiResponse;
import com.ssafy.project.domain.gallery.exception.UploadFailException;
import com.ssafy.project.domain.member.exception.InvalidPasswordException;
import com.ssafy.project.domain.member.exception.NotFoundMemberException;

import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;


// 예외가 발생하면 이 클래스에서 담당.
@RestControllerAdvice(annotations = RestController.class) // @RestController Annotation이 붙은 모든 클래스 대상.
public class ControllerExceptionHandler {

    // 어떤 예외가 발생하면 어떻게 응답을 해줄지.
    @ExceptionHandler(value = {NotFoundMemberException.class})
    public ResponseEntity<?> notFoundUserException() {
        ErrorCode code = ErrorCode.NOT_FOUND_USER;
        ApiResponse<?> errorResponse = ApiResponse.createError(code.getMessage());
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(errorResponse);
    }

    // 키가 중복되는 멤버 삽입 시도
    @ExceptionHandler(value = {DuplicateMemberException.class})
    public ResponseEntity<?> duplicateUserException() {
        ErrorCode code = ErrorCode.DUPLICATE_MEMBER;
        ApiResponse<?> errorResponse = ApiResponse.createError(code.getMessage());
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(errorResponse);
    }

    // 유효하지 않은 비밀번호 입력
    @ExceptionHandler(value = {InvalidPasswordException.class})
    public ResponseEntity<?> invalidPasswordException() {
        ErrorCode code = ErrorCode.INVALID_PASSWORD;
        ApiResponse<?> errorResponse = ApiResponse.createError(code.getMessage());
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(errorResponse);
    }

    // 파일 업로드 예외
    @ExceptionHandler(UploadFailException.class)
    public ResponseEntity<?> handleUploadFailException(UploadFailException e) {
        ErrorCode code = ErrorCode.UPLOAD_FAIL;
        ApiResponse<?> errorResponse = ApiResponse.createError(code.getMessage());
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(errorResponse);
    }

}
