package com.nexus_cart.microservices.Shop_microservice.dto;

import java.math.BigDecimal;

public class CartItemResponse {
	private String id;
	private String productId;
	private String productName;
	private BigDecimal untiPrice;
	private int quantity;
	private BigDecimal totalPrice;

	/**
	 * @param id
	 * @param productId
	 * @param productName
	 * @param untiPrice
	 * @param quantity
	 * @param totalPrice
	 */
	public CartItemResponse(String id, String productId, String productName, BigDecimal untiPrice, int quantity,
			BigDecimal totalPrice) {
		super();
		this.id = id;
		this.productId = productId;
		this.productName = productName;
		this.untiPrice = untiPrice;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
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
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the untiPrice
	 */
	public BigDecimal getUntiPrice() {
		return untiPrice;
	}

	/**
	 * @param untiPrice the untiPrice to set
	 */
	public void setUntiPrice(BigDecimal untiPrice) {
		this.untiPrice = untiPrice;
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
	 * @return the totalPrice
	 */
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	/**
	 * @param totalPrice the totalPrice to set
	 */
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

}
