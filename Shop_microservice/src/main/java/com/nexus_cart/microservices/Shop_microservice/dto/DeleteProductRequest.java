package com.nexus_cart.microservices.Shop_microservice.dto;

import jakarta.validation.constraints.NotBlank;

public class DeleteProductRequest {
	@NotBlank(message = "Id is required")
	private String id;

	/**
	 * @param id
	 */
	public DeleteProductRequest(@NotBlank String id) {
		super();
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

}
