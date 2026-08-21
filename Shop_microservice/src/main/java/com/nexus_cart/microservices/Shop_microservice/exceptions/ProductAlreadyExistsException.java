package com.nexus_cart.microservices.Shop_microservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProductAlreadyExistsException extends RuntimeException {

	/**
	 * @param message
	 */
	public ProductAlreadyExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
