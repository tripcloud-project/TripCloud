package com.ssafy.project.domain.gallery.exception;

public class UpdateDescriptionNotAllowedException extends RuntimeException{
	public UpdateDescriptionNotAllowedException(String message) {
		super(message);
	}
}
