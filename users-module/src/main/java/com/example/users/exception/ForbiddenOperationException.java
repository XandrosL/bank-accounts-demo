package com.example.users.exception;

//TODO Could move to a third, new "common" module and add as dependency of all modules to reuse code.
public class ForbiddenOperationException extends RuntimeException {

	private static final long serialVersionUID = -8919458079687208572L;

	public ForbiddenOperationException() {
		super();
	}

	public ForbiddenOperationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ForbiddenOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ForbiddenOperationException(String message) {
		super(message);
	}

	public ForbiddenOperationException(Throwable cause) {
		super(cause);
	}
}
