package com.nexus_cart.microservices.walet_microservice.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>{
	Optional<User> findByEmail(String email);

}
