package com.nexus_cart.microservices.inventory_microservice.inventory;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexus_cart.microservices.inventory_microservice.exceptions.InsufficientStockException;
import com.nexus_cart.microservices.inventory_microservice.exceptions.ProductNotFoundException;

@Service
public class InventoryService {
	@Autowired
	private InventoryRepository inventoryRepository;

	// Create/Add Stock
	public Inventory createAddStock(String productId, int quantity) {
		if (quantity <= 0) {
			throw new InsufficientStockException("Amount must be bigger than 0");
		}
		
		Optional<Inventory> inventory = inventoryRepository.findByProductId(productId);
		
		// New Product
		if (inventory.isEmpty()) {
			Inventory newInventory = new Inventory(null, productId, quantity);
			inventoryRepository.save(newInventory);
			
			return newInventory;
		}
		// Already exists
		else{
			int newQuantity = inventory.get().getQuantity() + quantity;
			inventory.get().setQuantity(newQuantity);
			
			inventoryRepository.save(inventory.get());
			
			return inventory.get();
		}
	}

	// Get Stock by Product ID
	public Inventory getStockByProductId(String productId) {
		Inventory inventory = inventoryRepository.findByProductId(productId)
				.orElseThrow(() -> new ProductNotFoundException("No product with this id"));

		return inventory;
	}

	// Deduct/Reduce Stock
	public Inventory deductStock(String productId, int amount) {
		if (amount <= 0) {
			throw new InsufficientStockException("Amount must be bigger than 0");
		}
		
		Inventory inventory = inventoryRepository.findByProductId(productId)
				.orElseThrow(() -> new ProductNotFoundException("No product with this id"));

		if (inventory.getQuantity() < amount) {
			throw new InsufficientStockException("Not enough stock available");
		}
		
		int newQuantity = inventory.getQuantity() - amount;
		inventory.setQuantity(newQuantity);
		
		inventoryRepository.save(inventory);

		return inventory;
	}
}
