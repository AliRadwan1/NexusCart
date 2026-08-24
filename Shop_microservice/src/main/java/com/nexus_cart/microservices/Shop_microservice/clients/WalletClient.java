package com.nexus_cart.microservices.Shop_microservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nexus_cart.microservices.Shop_microservice.dto.WalletTransactionRequest;
import com.nexus_cart.microservices.Shop_microservice.dto.WalletTransactionResponse;

@FeignClient(name = "wallet-service", url = "http://localhost:8081")
public interface WalletClient {
	@PostMapping("/wallets/withdraw")
	WalletTransactionResponse withdraw(@RequestBody WalletTransactionRequest request);
	
	@PostMapping("/wallets/deposit")
	WalletTransactionResponse deposit(@RequestBody WalletTransactionRequest request);
}
