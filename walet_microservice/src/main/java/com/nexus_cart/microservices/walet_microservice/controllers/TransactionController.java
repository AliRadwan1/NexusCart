package com.nexus_cart.microservices.walet_microservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable String userId) {
		Wallet wallet = walletRepository.findByUserId(userId).orElseThrow(() -> new WalletNotFoundException(userId));

		List<Transaction> history = transactionRepository.findByWalletIdOrderByCreatedAtDesc(wallet.getId());

		return ResponseEntity.ok(history);
	}

}
