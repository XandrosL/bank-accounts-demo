package com.example.bankaccounts.config;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.bankaccounts.exception.ForbiddenOperationException;
import com.example.bankaccounts.exception.ResourceNotFoundException;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class BankAccountsExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ IllegalArgumentException.class, ResourceNotFoundException.class,
			ForbiddenOperationException.class, HttpClientErrorException.class })
	protected ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		if (ex instanceof IllegalArgumentException || ex instanceof HttpClientErrorException) {
			statusCode = HttpStatus.BAD_REQUEST;
		} else if (ex instanceof ResourceNotFoundException) {
			statusCode = HttpStatus.NOT_FOUND;
		} else if (ex instanceof ForbiddenOperationException) {
			statusCode = HttpStatus.FORBIDDEN;
		}
		return handleExceptionInternal(ex, ex.getLocalizedMessage(), headers, statusCode, request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		HttpStatus statusCode = HttpStatus.BAD_REQUEST;
		Map<String, String> errors = new HashMap<>();
		ex.getConstraintViolations().forEach(error -> {
			String fieldName = error.getPropertyPath().toString();
			String errorMessage = error.getMessage();
			errors.put(fieldName, errorMessage);
		});
		return handleExceptionInternal(ex, errors, headers, statusCode, request);
	}

	@ExceptionHandler(ConnectException.class)
	protected ResponseEntity<Object> handleConnect(ConnectException ex, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		return handleExceptionInternal(ex,
				"Could not connect to depended service:" + System.lineSeparator() + ex.getLocalizedMessage(), headers,
				statusCode, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return handleExceptionInternal(ex, errors, headers, status, request);
	}
}