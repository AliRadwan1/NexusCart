package com.nexus_cart.microservices.walet_microservice.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus_cart.microservices.walet_microservice.dto.TransferRequest;
import com.nexus_cart.microservices.walet_microservice.dto.WalletTransactionRequest;
import com.nexus_cart.microservices.walet_microservice.dto.WalletTransactionResponse;
import com.nexus_cart.microservices.walet_microservice.wallets.Wallet;
import com.nexus_cart.microservices.walet_microservice.wallets.WalletService;

import jakarta.validation.Valid;

/**
 * REST Controller exposing HTTP API endpoints for retrieving wallet details, 
 * performing balance deposits and withdrawals, and executing transfers.
 */
@RestController
@RequestMapping("/wallets")
public class WalletController {

	@Autowired
	private WalletService walletService;

	/**
	 * Retrieves the wallet account corresponding to a user ID.
	 *
	 * @param userId Unique identifier of the user.
	 * @return {@link ResponseEntity} containing the requested {@link Wallet}.
	 */
	@GetMapping("/user/{userId}")
	public ResponseEntity<Wallet> retrieveWalletByUserId(@PathVariable String userId) {
		Wallet wallet = walletService.getWalletByUserId(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(wallet);
	}

	/**
	 * Adds funds to a user's wallet balance.
	 *
	 * @param request Validated {@link WalletTransactionRequest} payload containing user ID and amount.
	 * @return {@link ResponseEntity} containing the updated {@link WalletTransactionResponse}.
	 */
	@PostMapping("/deposit")
	public ResponseEntity<WalletTransactionResponse> deposit(@Valid @RequestBody WalletTransactionRequest request) {
		WalletTransactionResponse updatedWallet = walletService.deposit(request.getUserId(), request.getAmount());
		
		return ResponseEntity.status(HttpStatus.OK).body(updatedWallet);
	}

	/**
	 * Withdraws funds from a user's wallet balance and provides transaction details.
	 *
	 * @param request Validated {@link WalletTransactionRequest} payload containing user ID and amount.
	 * @return {@link ResponseEntity} containing the {@link WalletTransactionResponse} with generated transaction metadata.
	 */
	@PostMapping("/withdraw")
	public ResponseEntity<WalletTransactionResponse> withdraw(@Valid @RequestBody WalletTransactionRequest request) {
		WalletTransactionResponse response = walletService.withdraw(request.getUserId(), request.getAmount());
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * Executes a peer-to-peer balance transfer between two users.
	 *
	 * @param request Validated {@link TransferRequest} payload containing sender, receiver, and amount details.
	 * @return {@link ResponseEntity} with a success status message payload.
	 */
	@PostMapping("/transfer")
	public ResponseEntity<Map<String, String>> transfer(@Valid @RequestBody TransferRequest request) {
		walletService.transfer(
				request.getSenderUserId(), 
				request.getReceiverUserId(), 
				request.getAmount()
		);
		
		return ResponseEntity.ok(Map.of("message", "Transfer completed successfully"));
	}
}