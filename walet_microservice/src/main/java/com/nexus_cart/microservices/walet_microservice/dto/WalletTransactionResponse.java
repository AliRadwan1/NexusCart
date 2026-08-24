package com.nexus_cart.microservices.walet_microservice.dto;

import java.math.BigDecimal;

public class WalletTransactionResponse {
	private String transactionId;
	private String walletId;
	private BigDecimal amount;
	private BigDecimal balance;

	public WalletTransactionResponse() {
		super();
	}

	/**
	 * @param transactionId
	 * @param walletId
	 * @param amount
	 * @param balance
	 */
	public WalletTransactionResponse(String transactionId, String walletId, BigDecimal amount, BigDecimal balance) {
		super();
		this.transactionId = transactionId;
		this.walletId = walletId;
		this.amount = amount;
		this.balance = balance;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the walletId
	 */
	public String getWalletId() {
		return walletId;
	}

	/**
	 * @param walletId the walletId to set
	 */
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
