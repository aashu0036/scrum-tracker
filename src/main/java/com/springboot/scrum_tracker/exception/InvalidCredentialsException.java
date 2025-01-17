package com.springboot.scrum_tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidCredentialsException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;

	public InvalidCredentialsException(String userName) {
		super("Entered credentials not valid for user: "+ userName);
		this.userName=userName;
	}
	public String getUserName() {
		return userName;
	}
}
