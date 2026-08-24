package com.nexus_cart.microservices.Shop_microservice.dto;

public class InventoryRequest {
	private String productId;
	private int quantity;

	public InventoryRequest() {
	}

	public InventoryRequest(String productId, int quantity) {
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