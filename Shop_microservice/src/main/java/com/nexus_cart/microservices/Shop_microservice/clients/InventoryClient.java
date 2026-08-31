package com.nexus_cart.microservices.Shop_microservice.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nexus_cart.microservices.Shop_microservice.dto.InventoryRequest;
import com.nexus_cart.microservices.Shop_microservice.dto.InventoryResponse;

@FeignClient(name = "inventory-microservice", url = "${inventory.service.url:http://localhost:8082}")
public interface InventoryClient {

	@GetMapping("/api/inventory/{productId}")
    InventoryResponse getStockLevel(@PathVariable("productId") String productId);

    @GetMapping("/api/inventory")
    List<InventoryResponse> getAllInventory();

    @PostMapping("/api/inventory/create")
    InventoryResponse createStock(@RequestBody InventoryRequest request);

    @PostMapping("/api/inventory/add")
    InventoryResponse addStock(@RequestBody InventoryRequest request);

    @PostMapping("/api/inventory/deduct")
    InventoryResponse deductStock(@RequestBody InventoryRequest request);
}