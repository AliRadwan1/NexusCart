package com.nexus_cart.microservices.walet_microservice.exceptions;

public class WalletAlreadyExistsException extends RuntimeException {

	public WalletAlreadyExistsException(String message) {
		super(message);
	}
	
}
