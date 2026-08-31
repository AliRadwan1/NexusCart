package com.nexus_cart.microservices.Shop_microservice.dto;

import java.time.LocalDateTime;

public class InventoryResponse {
	private String productId;
	private Integer quantity;
	private LocalDateTime updatedAt;

	public InventoryResponse() {
	}

	public InventoryResponse(String productId, Integer quantity, LocalDateTime updatedAt) {
		this.productId = productId;
		this.quantity = quantity;
		this.updatedAt = updatedAt;
	}

	public String getProductId() {
		return productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}