package com.ssafy.project.domain.board.exception;

public class CommentInsertNotAllowedException extends RuntimeException{
	public CommentInsertNotAllowedException(String message) {
		super(message);
	}
}
