package com.nexus_cart.microservices.inventory_microservice.inventory;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory {

	@Id
	@Column(name = "product_id", nullable = false, unique = true)
	private String productId;

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public Inventory() {
	}
	
	public Inventory(String productId, Integer quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	@PrePersist
	@PreUpdate
	protected void onSaveOrUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	// Getters and Setters
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}