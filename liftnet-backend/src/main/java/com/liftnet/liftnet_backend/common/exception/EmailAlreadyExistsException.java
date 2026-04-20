package com.liftnet.liftnet_backend.common.exception;

public class EmailAlreadyExistsException extends RuntimeException {


	private static final long serialVersionUID = 3941683351583799024L;

	public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
