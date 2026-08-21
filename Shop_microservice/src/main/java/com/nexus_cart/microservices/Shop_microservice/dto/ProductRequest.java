package com.nexus_cart.microservices.Shop_microservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * 
 */
public class ProductRequest {
	@NotBlank
	private String name;

	@NotBlank
	private String category;

	@NotNull
	@Positive
	private BigDecimal price;

	public ProductRequest() {
		super();
	}

	/**
	 * 
	 * @param name
	 * @param category
	 * @param price
	 */
	public ProductRequest(@NotBlank String name, @NotBlank String category, @NotNull @Positive BigDecimal price) {
		super();
		this.name = name;
		this.category = category;
		this.price = price;
	}

	/**
	 * 
	 * @return name of the product
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * 
	 * @param category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * 
	 * @return price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * 
	 * @param price
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
