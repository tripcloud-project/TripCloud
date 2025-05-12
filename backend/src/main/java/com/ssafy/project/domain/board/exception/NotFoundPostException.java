package com.ssafy.project.domain.board.exception;

public class NotFoundPostException extends RuntimeException{
	public NotFoundPostException(String message) {
		super(message);
	}
}
