package com.springboot.scrum_tracker.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	//Handling specific exceptions first
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        ErrorDetails errorDetails=new ErrorDetails(
        		LocalDateTime.now(),
        		exception.getMessage(),
        		webRequest.getDescription(false),
        		"RESOURCE_NOT_FOUND"
        );
        log.error("Resource: {} with {} = {} not found", exception.getResourceName(), exception.getFieldName(), exception.getFieldValue());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleInvalidCredentialsException(InvalidCredentialsException exception, WebRequest webRequest) {
        ErrorDetails errorDetails=new ErrorDetails(
        		LocalDateTime.now(),
        		exception.getMessage(),
        		webRequest.getDescription(false),
        		"INVALID_CREDENTIALS"
        );
        log.error("Invalid Credentials");
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
	
	 
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorDetails> handleJwtException(ExpiredJwtException exception, WebRequest webRequest){
		ErrorDetails errorDetails=new ErrorDetails(
        		LocalDateTime.now(),
        		exception.getMessage(),
        		webRequest.getDescription(false),
        		"INTERNAL_SERVER_ERROR"
        );
		log.error("Expired JWT found!!");
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest webRequest) {
		
		 String errorMessages = ex.getFieldErrors()
		            .stream()
		            .map(FieldError::getDefaultMessage)  // Get the default message
		            .collect(Collectors.joining("  "));
		 ErrorDetails errorDetails=new ErrorDetails(
	        		LocalDateTime.now(),
	        		errorMessages,
	        		webRequest.getDescription(false),
	        		"BAD_REQUEST"
	        );
		 log.error("MethodArgumentNotValidException");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorDetails> handleJsonParseException(HttpMessageNotReadableException exception, WebRequest webRequest){
		ErrorDetails errorDetails=new ErrorDetails(
        		LocalDateTime.now(),
        		"JSON payload cannot be parsed. Please verify your request params",
        		webRequest.getDescription(false),
        		"INTERNAL_SERVER_ERROR"
        );
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(CustomProjectException.class)
	public ResponseEntity<ErrorDetails> handleCustomProjectException(CustomProjectException exception, WebRequest webRequest){
		ErrorDetails errorDetails=new ErrorDetails(
        		LocalDateTime.now(),
        		exception.getMessage(),
        		webRequest.getDescription(false),
        		exception.getHttpStatus().toString()
        );
		return new ResponseEntity<>(errorDetails, exception.getHttpStatus());
	}
	
	@ExceptionHandler(CustomIssueException.class)
	public ResponseEntity<ErrorDetails> handleCustomIssueException(CustomIssueException exception, WebRequest webRequest){
		ErrorDetails errorDetails=new ErrorDetails(
        		LocalDateTime.now(),
        		exception.getMessage(),
        		webRequest.getDescription(false),
        		exception.getHttpStatus().toString()
        );
		return new ResponseEntity<>(errorDetails, exception.getHttpStatus());
	}
	
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorDetails> handleCustomException(CustomException exception, WebRequest webRequest){
		ErrorDetails errorDetails=new ErrorDetails(
        		LocalDateTime.now(),
        		exception.getMessage(),
        		webRequest.getDescription(false),
        		exception.getHttpStatus().toString()
        );
		return new ResponseEntity<>(errorDetails, exception.getHttpStatus());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> handleGenericException(Exception exception, WebRequest webRequest){
		ErrorDetails errorDetails=new ErrorDetails(
        		LocalDateTime.now(),
        		"Oops! Something went wrong!",
        		webRequest.getDescription(false),
        		"INTERNAL_SERVER_ERROR"
        );
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
