package com.nexus_cart.microservices.walet_microservice.dto;

import java.math.BigDecimal;

public class WalletResponse {
	private String id;
	private String userId;
	private BigDecimal balance;

	/**
	 * @param id
	 * @param userId
	 * @param balance
	 */
	public WalletResponse(String id, String userId, BigDecimal balance) {
		super();
		this.id = id;
		this.userId = userId;
		this.balance = balance;
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
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}
