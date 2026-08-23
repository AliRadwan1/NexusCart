package com.nexus_cart.microservices.Shop_microservice.orders;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
	List<Order> findByUserId(String userId);
}