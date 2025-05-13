package com.ssafy.project.domain.board.exception;

public class PostUpdateNotAllowedException extends RuntimeException {
    public PostUpdateNotAllowedException(String message) {
        super(message);
    }
}
