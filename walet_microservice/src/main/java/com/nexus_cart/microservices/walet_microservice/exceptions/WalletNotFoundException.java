package com.nexus_cart.microservices.walet_microservice.exceptions;

public class WalletNotFoundException extends RuntimeException {

	public WalletNotFoundException(String message) {
		super(message);
	}

}
