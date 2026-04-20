package com.liftnet.liftnet_backend.common.exception;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5246203004994476750L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}