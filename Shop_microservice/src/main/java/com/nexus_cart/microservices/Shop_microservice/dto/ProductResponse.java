package com.nexus_cart.microservices.Shop_microservice.dto;

import java.math.BigDecimal;

public class ProductResponse {
	private String id;
	private String name;
	private String category;
	private BigDecimal price;

	public ProductResponse() {
		super();
	}

	/**
	 * @param id
	 * @param name
	 * @param category
	 * @param price
	 */
	public ProductResponse(String id, String name, String category, BigDecimal price) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.price = price;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
