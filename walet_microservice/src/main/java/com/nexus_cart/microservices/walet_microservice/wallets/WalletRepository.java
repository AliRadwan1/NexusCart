package com.nexus_cart.microservices.walet_microservice.wallets;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, String> {
	Optional<Wallet> findByUserId(String userId);
}
