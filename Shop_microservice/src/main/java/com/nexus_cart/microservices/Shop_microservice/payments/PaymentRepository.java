package com.nexus_cart.microservices.Shop_microservice.payments;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
	Optional<Payment> findByOrderId(String orderId);
}
