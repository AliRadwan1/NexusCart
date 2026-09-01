package com.nexus_cart.microservices.walet_microservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus_cart.microservices.walet_microservice.dto.WalletResponse;
import com.nexus_cart.microservices.walet_microservice.dto.WalletTransactionRequest;
import com.nexus_cart.microservices.walet_microservice.dto.WalletTransactionResponse;
import com.nexus_cart.microservices.walet_microservice.wallets.Wallet;
import com.nexus_cart.microservices.walet_microservice.wallets.WalletService;

import jakarta.validation.Valid;

/**
 * REST Controller exposing HTTP API endpoints for retrieving wallet details,
 * and performing balance deposits and withdrawals.
 * <p>
 * Enforces IDOR protection by deriving active user identities directly 
 * from the Spring {@link Authentication} security principal.
 */
@RestController
@RequestMapping("/wallets")
public class WalletController {

	@Autowired
	private WalletService walletService;

	/**
	 * Retrieves the current wallet account details for the authenticated user.
	 *
	 * @param authentication Current security context containing user principal claims.
	 * @return {@link ResponseEntity} containing the sanitized {@link WalletResponse}.
	 */
	@GetMapping("/balance")
	public ResponseEntity<WalletResponse> retrieveWalletByUserId(Authentication authentication) {
		String userId = getAuthenticatedUserId(authentication);
		
		Wallet wallet = walletService.getWalletByUserId(userId);
		WalletResponse response = new WalletResponse(wallet.getId(), wallet.getUserId(), wallet.getBalance());
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * Adds funds to the authenticated user's wallet balance.
	 *
	 * @param authentication Current security context containing user principal claims.
	 * @param request Validated {@link WalletTransactionRequest} payload containing amount.
	 * @return {@link ResponseEntity} containing the updated {@link WalletTransactionResponse}.
	 */
	@PostMapping("/deposit")
	public ResponseEntity<WalletTransactionResponse> deposit(Authentication authentication, @Valid @RequestBody WalletTransactionRequest request) {
		request.setUserId(getAuthenticatedUserId(authentication));
		
		WalletTransactionResponse updatedWallet = walletService.deposit(request);
		return ResponseEntity.status(HttpStatus.OK).body(updatedWallet);
	}

	/**
	 * Withdraws funds from the authenticated user's wallet balance.
	 *
	 * @param authentication Current security context containing user principal claims.
	 * @param request Validated {@link WalletTransactionRequest} payload containing amount.
	 * @return {@link ResponseEntity} containing the {@link WalletTransactionResponse} with transaction metadata.
	 */
	@PostMapping("/withdraw")
	public ResponseEntity<WalletTransactionResponse> withdraw(Authentication authentication, @Valid @RequestBody WalletTransactionRequest request) {
		request.setUserId(getAuthenticatedUserId(authentication));
		
		WalletTransactionResponse response = walletService.withdraw(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	/**
	 * Extracts and validates the authenticated user ID from the Spring {@link Authentication} principal.
	 *
	 * @param authentication Current security context containing user principal claims.
	 * @return The authenticated user's unique identifier string.
	 * @throws IllegalStateException If the security context or principal is missing/unauthenticated.
	 */
	private String getAuthenticatedUserId(Authentication authentication) {
		if (authentication == null || authentication.getPrincipal() == null) {
			throw new IllegalStateException("Unauthenticated user context");
		}
		return (String) authentication.getPrincipal();
	}
}