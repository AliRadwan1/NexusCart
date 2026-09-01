package com.nexus_cart.microservices.walet_microservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class WalletTransactionRequest {
//	@NotBlank
	private String userId;
	
	@NotNull
	@Positive
	private BigDecimal amount;

	public WalletTransactionRequest() {
		super();
	}

	public WalletTransactionRequest(String userId, BigDecimal amount) {
		super();
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
