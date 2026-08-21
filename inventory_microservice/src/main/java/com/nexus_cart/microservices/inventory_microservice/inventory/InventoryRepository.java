package com.nexus_cart.microservices.inventory_microservice.inventory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, String> {
	Optional<Inventory> findByProductId(String productId);
}
