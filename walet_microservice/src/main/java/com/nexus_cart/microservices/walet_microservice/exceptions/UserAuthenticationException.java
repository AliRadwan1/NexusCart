package com.nexus_cart.microservices.walet_microservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserAuthenticationException extends RuntimeException {
	public UserAuthenticationException(String message) {
		super(message);
	}

}
