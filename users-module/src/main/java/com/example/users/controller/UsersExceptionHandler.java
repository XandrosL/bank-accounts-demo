package com.example.users.controller;

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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.users.exception.ResourceNotFoundException;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class UsersExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ IllegalArgumentException.class, ResourceNotFoundException.class })
	protected ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		if (ex instanceof IllegalArgumentException) {
			statusCode = HttpStatus.BAD_REQUEST;
		} else if (ex instanceof ResourceNotFoundException) {
			statusCode = HttpStatus.NOT_FOUND;
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