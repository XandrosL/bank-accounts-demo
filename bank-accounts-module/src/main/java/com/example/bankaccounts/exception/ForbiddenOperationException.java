package com.example.bankaccounts.exception;

public class ForbiddenOperationException extends RuntimeException {

	private static final long serialVersionUID = -3573490017251026251L;

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