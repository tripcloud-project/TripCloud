package com.ssafy.project.domain.member.exception;

public class BadgeNotFoundException extends RuntimeException {
	public BadgeNotFoundException(String message) {
		super(message);
	}
}
