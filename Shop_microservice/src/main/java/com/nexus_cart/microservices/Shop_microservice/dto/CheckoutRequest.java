package com.nexus_cart.microservices.Shop_microservice.dto;

import jakarta.validation.constraints.NotBlank;

public class CheckoutRequest {
	@NotBlank
	private String userId;

	/**
	 * @param userId
	 */
	public CheckoutRequest(@NotBlank String userId) {
		super();
		this.userId = userId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
