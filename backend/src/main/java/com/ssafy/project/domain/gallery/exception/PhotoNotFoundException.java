package com.ssafy.project.domain.gallery.exception;

public class PhotoNotFoundException extends RuntimeException{
	public PhotoNotFoundException(String message) {
		super(message);
	}
}
