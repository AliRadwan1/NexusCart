package com.nexus_cart.microservices.Shop_microservice.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpdateProductRequest {
	@NotBlank
	private String id;

	@NotBlank
	private String name;

	@NotBlank
	private String category;

	@NotNull
	@Positive
	private BigDecimal price;

	private String currency;
	private List<String> imageUrls;

	/**
	 * @param id
	 * @param name
	 * @param category
	 * @param price
	 * @param currency
	 * @param imageUrls
	 */
	public UpdateProductRequest(@NotBlank String id, @NotBlank String name, @NotBlank String category,
			@NotNull @Positive BigDecimal price, String currency, List<String> imageUrls) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.price = price;
		this.currency = currency;
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
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
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
