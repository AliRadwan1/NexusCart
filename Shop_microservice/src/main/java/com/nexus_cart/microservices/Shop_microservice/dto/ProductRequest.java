package com.nexus_cart.microservices.Shop_microservice.dto;

import java.math.BigDecimal;
import java.util.List;

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
	
	private String currency;	
	private int initialStock;
	private List<String> imageUrls;

	public ProductRequest() {
		super();
	}

	/**
	 * @param name
	 * @param category
	 * @param price
	 * @param currency
	 * @param initialStock
	 * @param imageUrls
	 */
	public ProductRequest(@NotBlank String name, @NotBlank String category, @NotNull @Positive BigDecimal price,
			@NotBlank String currency, int initialStock, List<String> imageUrls) {
		super();
		this.name = name;
		this.category = category;
		this.price = price;
		this.currency = currency;
		this.initialStock = initialStock;
		this.imageUrls = imageUrls;
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
	 * @return the initialStock
	 */
	public int getInitialStock() {
		return initialStock;
	}

	/**
	 * @param initialStock the initialStock to set
	 */
	public void setInitialStock(int initialStock) {
		this.initialStock = initialStock;
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
