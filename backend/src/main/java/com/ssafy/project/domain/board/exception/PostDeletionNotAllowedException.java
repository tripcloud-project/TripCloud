package com.ssafy.project.domain.board.exception;

public class PostDeletionNotAllowedException extends RuntimeException {
    public PostDeletionNotAllowedException(String message) {
        super(message);
    }
}
