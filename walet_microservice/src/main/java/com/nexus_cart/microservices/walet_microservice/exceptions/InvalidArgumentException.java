package com.nexus_cart.microservices.walet_microservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidArgumentException extends RuntimeException {

	/**
	 * @param message
	 */
	public InvalidArgumentException(String message) {
		super(message);
	}

}
