package com.nexus_cart.microservices.Shop_microservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nexus_cart.microservices.Shop_microservice.dto.InventoryRequest;

@FeignClient(name = "inventory-microservice", url = "http://localhost:8082")
public interface InventoryClient {

	@GetMapping("/api/inventory/{productId}")
	Object getStockLevel(@PathVariable("productId") String productId);

	@PostMapping("/api/inventory/deduct")
	Object deductStock(@RequestBody InventoryRequest request);
	
	@PostMapping("/api/inventory")
	Object createAddStock(@RequestBody InventoryRequest request);
}