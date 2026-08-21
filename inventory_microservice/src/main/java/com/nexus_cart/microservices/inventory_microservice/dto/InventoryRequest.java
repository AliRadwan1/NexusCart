package com.nexus_cart.microservices.inventory_microservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class InventoryRequest {
	@NotNull
	private String productId;
	
	@NotNull
	@Min(1)
	private int quantity;

	public InventoryRequest() {
		super();
	}

	public InventoryRequest(@NotNull String productId, @NotNull int quantity) {
		super();
		this.productId = productId;
		this.quantity = quantity;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
