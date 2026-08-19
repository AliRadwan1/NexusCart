package com.nexus_cart.microservices.walet_microservice.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus_cart.microservices.walet_microservice.dto.LoginRequest;
import com.nexus_cart.microservices.walet_microservice.dto.RegisterRequest;
import com.nexus_cart.microservices.walet_microservice.users.User;
import com.nexus_cart.microservices.walet_microservice.users.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody RegisterRequest request) {
		userService.registerUser(request.getFirstName(), request.getLastName(), request.getEmail(),
				request.getPassword());

		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","User registered successfully"));
	}

	@PostMapping("/login")
	public ResponseEntity<User> login(@Valid @RequestBody LoginRequest request) {
		User loginUser = userService.loginUser(request.getEmail(), request.getPassword());
		
		return ResponseEntity.ok(loginUser);
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> retrieveUserById(@PathVariable String id) {
		User user = userService.getUserId(id);

		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
}
