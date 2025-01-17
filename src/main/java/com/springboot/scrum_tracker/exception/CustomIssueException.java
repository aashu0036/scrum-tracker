package com.springboot.scrum_tracker.exception;

import org.springframework.http.HttpStatus;

public class CustomIssueException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HttpStatus httpStatus;
	
	public CustomIssueException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus=httpStatus;
	}
	
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	
}