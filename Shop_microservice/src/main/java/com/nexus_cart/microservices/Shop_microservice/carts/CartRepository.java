package com.nexus_cart.microservices.Shop_microservice.carts;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, String> {
	Optional<Cart> findByUserIdAndStatus(String userId, CartStatus status);
}
