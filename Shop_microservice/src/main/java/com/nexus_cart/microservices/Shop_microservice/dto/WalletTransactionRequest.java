package com.nexus_cart.microservices.Shop_microservice.dto;

import java.math.BigDecimal;

public class WalletTransactionRequest {
	private String userId;
	private BigDecimal amount;

	public WalletTransactionRequest() {
	}

	public WalletTransactionRequest(String userId, BigDecimal amount) {
		this.userId = userId;
		this.amount = amount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}