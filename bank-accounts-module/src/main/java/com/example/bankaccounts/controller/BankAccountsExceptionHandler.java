package com.example.bankaccounts.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.bankaccounts.exception.ForbiddenOperationException;
import com.example.bankaccounts.exception.ResourceNotFoundException;

@ControllerAdvice
public class BankAccountsExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ IllegalArgumentException.class, ResourceNotFoundException.class,
			ForbiddenOperationException.class })
	protected ResponseEntity<Object> handleException(RuntimeException e, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		if (e instanceof IllegalArgumentException) {
			statusCode = HttpStatus.BAD_REQUEST;
		} else if (e instanceof ResourceNotFoundException) {
			statusCode = HttpStatus.NOT_FOUND;
		} else if (e instanceof ForbiddenOperationException) {
			statusCode = HttpStatus.FORBIDDEN;
		}
		return handleExceptionInternal(e, e.getLocalizedMessage(), headers, statusCode, request);
	}
}