package com.example.bankaccounts.exception;

//TODO Could move to a third, new "common" module and add as dependency of all modules to reuse code.
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -4497863459451468498L;

	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}
}