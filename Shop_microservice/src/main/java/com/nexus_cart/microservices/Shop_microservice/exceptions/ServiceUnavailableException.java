package com.nexus_cart.microservices.Shop_microservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a downstream microservice (Wallet or Inventory) is unreachable
 * or has triggered a Resilience4j circuit breaker open state.
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException(String message) {
        super(message);
    }

    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}