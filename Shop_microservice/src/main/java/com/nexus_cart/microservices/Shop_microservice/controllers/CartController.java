package com.nexus_cart.microservices.Shop_microservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexus_cart.microservices.Shop_microservice.carts.CartService;
import com.nexus_cart.microservices.Shop_microservice.dto.AddToCartRequest;
import com.nexus_cart.microservices.Shop_microservice.dto.CartResponse;

import jakarta.validation.Valid;

/**
 * REST controller exposing secured endpoints for managing user shopping carts.
 * All operations derive user identity directly from the authenticated JWT SecurityContext.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	/**
	 * Helper method to retrieve userId from JWT SecurityContext.
	 */
	private String getAuthenticatedUserId(Authentication authentication) {
		return (String) authentication.getPrincipal();
	}

	/**
	 * Adds a product item to the authenticated user's active cart.
	 *
	 * @param authentication Current user authentication populated by JWT filter.
	 * @param request        Validated {@link AddToCartRequest} containing product ID and quantity.
	 * @return {@link ResponseEntity} containing updated {@link CartResponse} and HTTP 200 OK.
	 */
	@PostMapping
	public ResponseEntity<CartResponse> addItemToCart(Authentication authentication, 
			@Valid @RequestBody AddToCartRequest request) {
		String userId = getAuthenticatedUserId(authentication);
		request.setUserId(userId);
		
		CartResponse response = cartService.addToCart(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * Retrieves the active shopping cart for the authenticated user.
	 *
	 * @param authentication Current user authentication populated by JWT filter.
	 * @return {@link ResponseEntity} containing active {@link CartResponse} and HTTP 200 OK.
	 */
	@GetMapping
	public ResponseEntity<CartResponse> retrieveActiveUserCart(Authentication authentication) {
		String userId = getAuthenticatedUserId(authentication);
		CartResponse response = cartService.getCartByUserId(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * Adjusts the quantity of a specific product in the authenticated user's cart.
	 *
	 * @param authentication Current user authentication populated by JWT filter.
	 * @param productId      The unique identifier of the product.
	 * @param delta          Relative quantity change (e.g., +1, -1).
	 * @return {@link ResponseEntity} containing updated {@link CartResponse} and HTTP 200 OK.
	 */
	@PutMapping("/items/{productId}")
	public ResponseEntity<CartResponse> updateItemQuantity(Authentication authentication,
			@PathVariable String productId,
			@RequestParam int delta) {
		String userId = getAuthenticatedUserId(authentication);
		CartResponse response = cartService.updateItemQuantity(userId, productId, delta);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * Removes a specific product completely from the authenticated user's cart.
	 *
	 * @param authentication Current user authentication populated by JWT filter.
	 * @param productId      The unique identifier of the product to remove.
	 * @return {@link ResponseEntity} containing updated {@link CartResponse} and HTTP 200 OK.
	 */
	@DeleteMapping("/items/{productId}")
	public ResponseEntity<CartResponse> removeSingleProduct(Authentication authentication,
			@PathVariable String productId) {
		String userId = getAuthenticatedUserId(authentication);
		CartResponse response = cartService.removeItemFromCart(userId, productId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * Clears all items from the authenticated user's active shopping cart.
	 *
	 * @param authentication Current user authentication populated by JWT filter.
	 * @return {@link ResponseEntity} containing empty {@link CartResponse} and HTTP 200 OK.
	 */
	@DeleteMapping
	public ResponseEntity<CartResponse> clearActiveCart(Authentication authentication) {
		String userId = getAuthenticatedUserId(authentication);
		CartResponse response = cartService.clearCart(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}