package com.ssafy.project.common.response;

public record SuccessResponse<T>(int status, String message, T result) {
    public static <T> SuccessResponse<T> success(String message, T result) {
        return new SuccessResponse<>(200, message, result);
    }

    public static <T> SuccessResponse<T> successCreate(String message, T result) {
        return new SuccessResponse<>(201, message, result);
    }
}
