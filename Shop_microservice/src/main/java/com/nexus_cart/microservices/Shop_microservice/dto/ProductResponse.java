package com.nexus_cart.microservices.Shop_microservice.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductResponse {
	private String id;
	private String name;
	private String category;
	private BigDecimal price;
	private int quantity;
	private List<String> imageUrls;

	public ProductResponse() {
		super();
	}

	/**
	 * @param id
	 * @param name
	 * @param category
	 * @param price
	 * @param quantity
	 * @param imageUrls
	 */
	public ProductResponse(String id, String name, String category, BigDecimal price, int quantity,
			List<String> imageUrls) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.price = price;
		this.quantity = quantity;
		this.imageUrls = imageUrls;
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
	 * @return the imageUrls
	 */
	public List<String> getImageUrls() {
		return imageUrls;
	}

	/**
	 * @param imageUrls the imageUrls to set
	 */
	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

}
