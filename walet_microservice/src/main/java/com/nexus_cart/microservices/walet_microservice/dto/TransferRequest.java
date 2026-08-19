package com.nexus_cart.microservices.walet_microservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransferRequest {
	@NotBlank
	private String senderUserId;
	
	@NotBlank
	private String receiverUserId;
	
	@NotNull
	@Positive(message = "Transfer amount must be greater than zero")
	private BigDecimal amount;

	public TransferRequest() {
		super();
	}

	public TransferRequest(@NotBlank String senderUserId, @NotBlank String recieverUserId,
			@NotBlank BigDecimal amount) {
		super();
		this.senderUserId = senderUserId;
		this.receiverUserId = recieverUserId;
		this.amount = amount;
	}

	public String getSenderUserId() {
		return senderUserId;
	}

	public void setSenderUserId(String senderUserId) {
		this.senderUserId = senderUserId;
	}

	public String getReceiverUserId() {
		return receiverUserId;
	}

	public void setReceiverUserId(String recieverUserId) {
		this.receiverUserId = recieverUserId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
