package com.ssafy.project.domain.board.exception;

public class CommentDeletionNotAllowedException extends RuntimeException {
    public CommentDeletionNotAllowedException(String message) {
        super(message);
    }
}
