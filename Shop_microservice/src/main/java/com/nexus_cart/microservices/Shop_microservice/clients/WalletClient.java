package com.nexus_cart.microservices.Shop_microservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nexus_cart.microservices.Shop_microservice.dto.WalletTransactionRequest;

@FeignClient(name = "wallet_service", url = "http://localhost:8081")
public interface WalletClient {
	@PostMapping("/wallets/withdraw")
	Object withdraw(@RequestBody WalletTransactionRequest request);
	
	@PostMapping("/wallets/deposit")
	Object deposit(@RequestBody WalletTransactionRequest request);
}
