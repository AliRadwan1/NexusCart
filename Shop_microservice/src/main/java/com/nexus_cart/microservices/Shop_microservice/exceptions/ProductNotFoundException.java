package com.nexus_cart.microservices.Shop_microservice.exceptions;

public class ProductNotFoundException extends RuntimeException {
	/**
	 * @param message
	 */
	public ProductNotFoundException(String message) {
		super(message);
	}
}
