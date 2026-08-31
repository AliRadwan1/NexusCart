package com.nexus_cart.microservices.inventory_microservice.controllers;

import java.util.List;

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
import com.nexus_cart.microservices.inventory_microservice.inventory.InventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	@Autowired
	private InventoryService inventoryService;
	
	// 1. Create initial inventory record (called when creating a product)
    @PostMapping("/create")
    public ResponseEntity<InventoryResponse> createStock(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.createStock(request));
    }

    // 2. Add stock to existing product
    @PostMapping("/add")
    public ResponseEntity<InventoryResponse> addStock(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.addStock(request));
    }
    
    // 3. Deduct stock during checkout
    @PostMapping("/deduct")
    public ResponseEntity<InventoryResponse> deductStock(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.deductStock(request));
    }

    // 4. Get stock details for a single product
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getStockLevel(@PathVariable("productId") String productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }
    
    // 5. Get all inventory items
    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

}
