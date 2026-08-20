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
import com.nexus_cart.microservices.walet_microservice.wallets.Wallet;
import com.nexus_cart.microservices.walet_microservice.wallets.WalletService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/wallets")
public class WalletController {
	@Autowired
	private WalletService walletService;

	@GetMapping("/user/{userId}")
	public ResponseEntity<Wallet> retrieveWalletByUserId(@PathVariable String userId) {
		Wallet wallet = walletService.getWalletByUserId(userId);

		return ResponseEntity.status(HttpStatus.OK).body(wallet);
	}

	@PostMapping("/deposit")
	public ResponseEntity<Wallet> deposit(@Valid @RequestBody WalletTransactionRequest request) {
		Wallet updatedWallet = walletService.deposit(request.getUserId(), request.getAmount());
		
		return ResponseEntity.status(HttpStatus.OK).body(updatedWallet);
	}
	
	@PostMapping("/withdraw")
	public ResponseEntity<Wallet> withdraw(@Valid @RequestBody WalletTransactionRequest request) {
		Wallet updatedWallet = walletService.withdraw(request.getUserId(), request.getAmount());
		
		return ResponseEntity.status(HttpStatus.OK).body(updatedWallet);
	}
	
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
