package com.nexus_cart.microservices.walet_microservice.exceptions;

public class InsufficientBalanceException extends RuntimeException {

	public InsufficientBalanceException(String message) {
		super(message);
	}
}
