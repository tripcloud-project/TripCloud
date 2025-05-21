package com.ssafy.project.domain.gallery.exception;

public class DownloadFailException extends RuntimeException{
	public DownloadFailException(String message) {
		super(message);
	}
}
