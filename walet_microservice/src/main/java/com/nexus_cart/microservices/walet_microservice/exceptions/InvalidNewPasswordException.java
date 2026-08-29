package com.nexus_cart.microservices.walet_microservice.exceptions;

public class InvalidNewPasswordException extends RuntimeException {

	/**
	 * @param message
	 */
	public InvalidNewPasswordException(String message) {
		super(message);
	}

}
