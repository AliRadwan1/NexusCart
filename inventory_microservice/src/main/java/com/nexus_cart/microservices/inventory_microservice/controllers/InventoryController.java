package com.nexus_cart.microservices.inventory_microservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus_cart.microservices.inventory_microservice.dto.InventoryRequest;
import com.nexus_cart.microservices.inventory_microservice.dto.InventoryResponse;
import com.nexus_cart.microservices.inventory_microservice.inventory.Inventory;
import com.nexus_cart.microservices.inventory_microservice.inventory.InventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	@Autowired
	private InventoryService inventoryService;

	@PostMapping
	public ResponseEntity<InventoryResponse> createAddStock(@Valid @RequestBody InventoryRequest request) {
		Inventory inventory = inventoryService.createAddStock(request.getProductId(), request.getQuantity());

		InventoryResponse response = new InventoryResponse(inventory.getId(), inventory.getProductId(),
				inventory.getQuantity());

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{productId}")
	public ResponseEntity<InventoryResponse> getStockLevel(@PathVariable String productId) {
		Inventory inventory = inventoryService.getStockByProductId(productId);

		InventoryResponse response = new InventoryResponse(inventory.getId(), inventory.getProductId(),
				inventory.getQuantity());

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/deduct")
	public ResponseEntity<InventoryResponse> deductStock(@Valid @RequestBody InventoryRequest request) {
		Inventory inventory = inventoryService.deductStock(request.getProductId(), request.getQuantity());

		InventoryResponse response = new InventoryResponse(inventory.getId(), inventory.getProductId(),
				inventory.getQuantity());

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
