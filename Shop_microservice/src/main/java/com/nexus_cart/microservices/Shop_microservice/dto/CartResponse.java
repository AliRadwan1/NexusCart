package com.nexus_cart.microservices.Shop_microservice.dto;

import java.math.BigDecimal;
import java.util.List;

import com.nexus_cart.microservices.Shop_microservice.carts.CartStatus;

public class CartResponse {
	private String id;
	private String userId;
	private CartStatus status;
	private List<CartItemResponse> items;
	private BigDecimal grandTotal;

	/**
	 * @param id
	 * @param userId
	 * @param status
	 * @param items
	 * @param grandTotal
	 */
	public CartResponse(String id, String userId, CartStatus status, List<CartItemResponse> items,
			BigDecimal grandTotal) {
		super();
		this.id = id;
		this.userId = userId;
		this.status = status;
		this.items = items;
		this.grandTotal = grandTotal;
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

	/**
	 * @return the status
	 */
	public CartStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(CartStatus status) {
		this.status = status;
	}

	/**
	 * @return the items
	 */
	public List<CartItemResponse> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<CartItemResponse> items) {
		this.items = items;
	}

	/**
	 * @return the grandTotal
	 */
	public BigDecimal getGrandTotal() {
		return grandTotal;
	}

	/**
	 * @param grandTotal the grandTotal to set
	 */
	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}

}
