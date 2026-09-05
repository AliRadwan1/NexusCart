package com.nexus_cart.microservices.inventory_microservice.inventory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexus_cart.microservices.inventory_microservice.dto.InventoryRequest;
import com.nexus_cart.microservices.inventory_microservice.dto.InventoryResponse;
import com.nexus_cart.microservices.inventory_microservice.exceptions.InsufficientStockException;
import com.nexus_cart.microservices.inventory_microservice.exceptions.ProductNotFoundException;

@Service
public class InventoryService {
	@Autowired
	private InventoryRepository inventoryRepository;

	// Create
	@Transactional
	public InventoryResponse createStock(InventoryRequest request) {
		if (inventoryRepository.existsById(request.getProductId())) {
			throw new IllegalStateException("Inventory entry already exists for product: " + request.getProductId());
		}

		Inventory inventory = new Inventory(request.getProductId(), request.getQuantity());
		Inventory saved = inventoryRepository.save(inventory);
		
		return mapToInventoryResponse(saved);
	}

	// Add Stock
	@Transactional
	public InventoryResponse addStock(InventoryRequest request) {
		Inventory inventory = inventoryRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Inventory not found for product: " + request.getProductId()));
		
		int newQuantity = inventory.getQuantity() + request.getQuantity();
		inventory.setQuantity(newQuantity);
		Inventory saved = inventoryRepository.save(inventory);
		
		return mapToInventoryResponse(saved);
	}

	// Deduct/Reduce Stock
	@Transactional
    public InventoryResponse deductStock(InventoryRequest request) {
		Inventory inventory = inventoryRepository.findById(request.getProductId())
				.orElseThrow(() -> new ProductNotFoundException("Inventory not found for product: " + request.getProductId()));
		
		if (inventory.getQuantity() < request.getQuantity()) {
			throw new InsufficientStockException("Insufficient stock for product: " + request.getProductId());
		}
		
		int newQuatity = inventory.getQuantity() - request.getQuantity();
		inventory.setQuantity(newQuatity);
		Inventory saved = inventoryRepository.save(inventory);
		
		return mapToInventoryResponse(saved);
	}
	
	// Get Stock by Product ID
	@Transactional(readOnly = true)
    public InventoryResponse getInventoryByProductId(String productId) {
        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Inventory not found for product: " + productId));

        return mapToInventoryResponse(inventory);
    }
	
	// Get all inventory
	@Transactional(readOnly = true)
    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(this::mapToInventoryResponse)
                .collect(Collectors.toList());
    }
	
	/**
	 * Helper function to convert into inventory response
	 * 
	 * @param inventory
	 * @return
	 */
	private InventoryResponse mapToInventoryResponse(Inventory inventory) {
        return new InventoryResponse(inventory.getProductId(), inventory.getQuantity(), inventory.getUpdatedAt());
    }
}
