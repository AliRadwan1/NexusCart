package com.nexus_cart.microservices.Shop_microservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {
	/**
	 * @param message
	 */
	public OrderNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
