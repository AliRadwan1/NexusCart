package com.nexus_cart.microservices.walet_microservice.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus_cart.microservices.walet_microservice.dto.AuthResponse;
import com.nexus_cart.microservices.walet_microservice.dto.ChangePasswordRequest;
import com.nexus_cart.microservices.walet_microservice.dto.DeleteAccountRequest;
import com.nexus_cart.microservices.walet_microservice.dto.LoginRequest;
import com.nexus_cart.microservices.walet_microservice.dto.RegisterRequest;
import com.nexus_cart.microservices.walet_microservice.dto.UpdateUserInfoRequest;
import com.nexus_cart.microservices.walet_microservice.dto.UserResponse;
import com.nexus_cart.microservices.walet_microservice.security.JwtUtils;
import com.nexus_cart.microservices.walet_microservice.users.User;
import com.nexus_cart.microservices.walet_microservice.users.UserService;

import jakarta.validation.Valid;

/**
 * REST controller responsible for managing user lifecycle operations, authentication,
 * profile modifications, and account deletions within the Wallet Microservice.
 * <p>
 * Implements strict IDOR protection by deriving active user identities directly 
 * from the Spring {@link Authentication} security context rather than client-supplied inputs.
 */
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtils jwtUtils;

	/**
	 * Registers a new user account in the system.
	 *
	 * @param request Payload containing user registration details and credentials.
	 * @return {@link ResponseEntity} containing a success message and HTTP 201 (Created) status.
	 */
	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody RegisterRequest request) {
		userService.registerUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User registered successfully"));
	}

	/**
	 * Authenticates user credentials and issues a signed JWT token.
	 *
	 * @param request Payload containing login credentials (email and password).
	 * @return {@link ResponseEntity} containing the JWT bearer token and non-sensitive {@link UserResponse} profile data.
	 */
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		User loginUser = userService.loginUser(request);
		String token = jwtUtils.generateToken(loginUser.getId(), loginUser.getEmail());

		UserResponse response = new UserResponse(loginUser.getFirstName(), loginUser.getLastName(), loginUser.getEmail());
		return ResponseEntity.ok(new AuthResponse(token, response));
	}

	/**
	 * Updates personal information for the currently authenticated user.
	 *
	 * @param authentication Current security context containing user principal claims.
	 * @param request Payload containing updated user profile details.
	 * @return {@link ResponseEntity} containing a success confirmation message.
	 */
	@PutMapping("/editInfo")
	public ResponseEntity<Map<String, String>> editInfo(Authentication authentication,
			@Valid @RequestBody UpdateUserInfoRequest request) {
		request.setId(getAuthenticatedUserId(authentication));
		userService.editUserInfo(request);

		return ResponseEntity.ok(Map.of("message", "User info has been updated successfully"));
	}

	/**
	 * Changes the account password for the currently authenticated user.
	 *
	 * @param authentication Current security context containing user principal claims.
	 * @param request Payload containing current and target password data.
	 * @return {@link ResponseEntity} containing a success confirmation message.
	 */
	@PutMapping("/changePassword")
	public ResponseEntity<Map<String, String>> changePassword(Authentication authentication,
			@Valid @RequestBody ChangePasswordRequest request) {
		request.setId(getAuthenticatedUserId(authentication));
		userService.changePassword(request);

		return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
	}

	/**
	 * Permanently deletes the account of the currently authenticated user.
	 *
	 * @param authentication Current security context containing user principal claims.
	 * @param request Payload containing deletion verification details.
	 * @return {@link ResponseEntity} containing an account deletion confirmation message.
	 */
	@PostMapping("/delete")
	public ResponseEntity<Map<String, String>> deleteAccount(Authentication authentication,
			@Valid @RequestBody DeleteAccountRequest request) {
		request.setId(getAuthenticatedUserId(authentication));
		userService.deleteAccount(request);

		return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
	}

	/**
	 * Fetches non-sensitive profile information for the currently authenticated user.
	 *
	 * @param authentication Current security context containing user principal claims.
	 * @return {@link ResponseEntity} containing the populated {@link UserResponse} DTO.
	 */
	@GetMapping("/profile")
	public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
		String id = getAuthenticatedUserId(authentication);
		User user = userService.getUserId(id);

		UserResponse response = new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail());
		return ResponseEntity.ok(response);
	}

	/**
	 * Extracts and validates the authenticated user ID from the Spring {@link Authentication} principal.
	 *
	 * @param authentication Current security context containing user principal claims.
	 * @return The authenticated user's unique identifier string.
	 * @throws IllegalStateException If the security context or principal is missing/unauthenticated.
	 */
	private String getAuthenticatedUserId(Authentication authentication) {
		if (authentication == null || authentication.getPrincipal() == null) {
			throw new IllegalStateException("Unauthenticated user context");
		}
		return (String) authentication.getPrincipal();
	}
}