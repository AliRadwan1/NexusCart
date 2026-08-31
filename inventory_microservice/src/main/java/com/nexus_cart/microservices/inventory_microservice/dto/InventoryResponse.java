package com.nexus_cart.microservices.inventory_microservice.dto;

import java.time.LocalDateTime;

public class InventoryResponse {
	private String productId;
	private int quantity;
	private LocalDateTime updatedAt;

	public InventoryResponse() {
	}

	public InventoryResponse(String productId, Integer quantity, LocalDateTime updatedAt) {
		this.productId = productId;
		this.quantity = quantity;
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the updatedAt
	 */
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
