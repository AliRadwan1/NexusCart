package com.nexus_cart.microservices.walet_microservice.transactions;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
	List<Transaction> findByWalletIdOrderByCreatedAtDesc(String walletId);
}
