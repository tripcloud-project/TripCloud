package com.ssafy.project.exception;


import com.ssafy.project.common.response.ApiResponse;
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

}
