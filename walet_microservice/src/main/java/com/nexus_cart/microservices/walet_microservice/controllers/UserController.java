package com.nexus_cart.microservices.walet_microservice.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus_cart.microservices.walet_microservice.dto.AuthResponse;
import com.nexus_cart.microservices.walet_microservice.dto.ChangePasswordRequest;
import com.nexus_cart.microservices.walet_microservice.dto.LoginRequest;
import com.nexus_cart.microservices.walet_microservice.dto.RegisterRequest;
import com.nexus_cart.microservices.walet_microservice.dto.UpdateUserInfoRequest;
import com.nexus_cart.microservices.walet_microservice.security.JwtUtils;
import com.nexus_cart.microservices.walet_microservice.users.User;
import com.nexus_cart.microservices.walet_microservice.users.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody RegisterRequest request) {
		userService.registerUser(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","User registered successfully"));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		User loginUser = userService.loginUser(request.getEmail(), request.getPassword());
		String token = jwtUtils.generateToken(loginUser.getId(), loginUser.getEmail());
		
		return ResponseEntity.ok(new AuthResponse(token, loginUser));
	}
	
	@PutMapping("/editInfo")
	public ResponseEntity<Map<String, String>> editInfo(@Valid @RequestBody UpdateUserInfoRequest request){
		userService.editUserInfo(request);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "User info has been updated successfully"));
	}
	
	@PutMapping("/changePassword")
	public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordRequest request){
		userService.changePassword(request);
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Password changed successfully"));
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> retrieveUserById(@PathVariable String id) {
		User user = userService.getUserId(id);

		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
}
