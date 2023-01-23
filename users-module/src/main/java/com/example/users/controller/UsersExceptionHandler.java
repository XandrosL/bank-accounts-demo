package com.example.users.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.users.exception.ResourceNotFoundException;

@ControllerAdvice
public class UsersExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ IllegalArgumentException.class, ResourceNotFoundException.class })
	protected ResponseEntity<Object> handleException(RuntimeException e, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		if (e instanceof IllegalArgumentException) {
			statusCode = HttpStatus.BAD_REQUEST;
		} else if (e instanceof ResourceNotFoundException) {
			statusCode = HttpStatus.NOT_FOUND;
		}
		return handleExceptionInternal(e, e.getLocalizedMessage(), headers, statusCode, request);
	}
}