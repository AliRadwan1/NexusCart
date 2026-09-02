package com.nexus_cart.microservices.walet_microservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus_cart.microservices.walet_microservice.exceptions.WalletNotFoundException;
import com.nexus_cart.microservices.walet_microservice.transactions.Transaction;
import com.nexus_cart.microservices.walet_microservice.transactions.TransactionRepository;
import com.nexus_cart.microservices.walet_microservice.wallets.Wallet;
import com.nexus_cart.microservices.walet_microservice.wallets.WalletRepository;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private WalletRepository walletRepository;

	/**
	 * Retrieves the transaction history for the currently authenticated user.
	 *
	 * @param authentication Current security context containing user principal claims.
	 * @return {@link ResponseEntity} containing a list of {@link Transaction} records.
	 */
	@GetMapping("/history")
	public ResponseEntity<List<Transaction>> getTransactionHistory(Authentication authentication) {
		String userId = getAuthenticatedUserId(authentication);

		Wallet wallet = walletRepository.findByUserId(userId)
				.orElseThrow(() -> new WalletNotFoundException(userId));

		List<Transaction> history = transactionRepository.findByWalletIdOrderByCreatedAtDesc(wallet.getId());

		return ResponseEntity.ok(history);
	}

	/**
	 * Extracts and validates the authenticated user ID from the Spring {@link Authentication} principal.
	 */
	private String getAuthenticatedUserId(Authentication authentication) {
		if (authentication == null || authentication.getPrincipal() == null) {
			throw new IllegalStateException("Unauthenticated user context");
		}
		return (String) authentication.getPrincipal();
	}

}
