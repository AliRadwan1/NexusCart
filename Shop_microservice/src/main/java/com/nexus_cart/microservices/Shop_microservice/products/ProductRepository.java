package com.nexus_cart.microservices.Shop_microservice.products;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

	// Used during creation to prevent duplicates
	boolean existsByName(String name);

	// Used for exact lookup
	Optional<Product> findByName(String name);

	// Used by frontend search bars (e.g., searching "phone" returns all matching items)
	List<Product> findByNameContainingIgnoreCase(String name);

	// Filter by category
	List<Product> findByCategoryIgnoreCase(String category);
}