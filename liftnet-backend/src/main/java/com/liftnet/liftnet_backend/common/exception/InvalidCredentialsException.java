package com.liftnet.liftnet_backend.common.exception;

public class InvalidCredentialsException extends RuntimeException {

	private static final long serialVersionUID = -7032476009250039223L;

	public InvalidCredentialsException(String message) {
        super(message);
    }
}