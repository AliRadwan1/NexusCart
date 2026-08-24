package com.nexus_cart.microservices.Shop_microservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * REST controller exposing endpoints for managing user shopping carts,
 * including adding items, updating quantities, fetching active carts, and
 * clearing items.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	/**
	 * Adds a product item to the user's active cart or increments its quantity if
	 * already present.
	 *
	 * @param request Validated {@link AddToCartRequest} containing user ID, product
	 *                ID, and quantity.
	 * @return {@link ResponseEntity} containing the updated {@link CartResponse}
	 *         and HTTP status 200 OK.
	 */
	@PostMapping
	public ResponseEntity<CartResponse> addItemToCart(@Valid @RequestBody AddToCartRequest request) {
		CartResponse response = cartService.addToCart(request);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * Retrieves the active shopping cart for a specific user.
	 *
	 * @param userId The unique identifier of the user.
	 * @return {@link ResponseEntity} containing the active {@link CartResponse} and
	 *         HTTP status 200 OK.
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<CartResponse> retrieveActiveUserCart(@PathVariable String userId) {
		CartResponse response = cartService.getCartByUserId(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * Adjusts the quantity of a specific product in the user's active cart by a
	 * relative delta. Positive values increment quantity; negative values decrement
	 * it.
	 *
	 * @param userId    The unique identifier of the user.
	 * @param productId The unique identifier of the product.
	 * @param delta     Relative quantity change (e.g., +1, -1).
	 * @return {@link ResponseEntity} containing the updated {@link CartResponse}
	 *         and HTTP status 200 OK.
	 */
	@PutMapping("/{userId}/items/{productId}")
	public ResponseEntity<CartResponse> updateItemQuantity(@PathVariable String userId, @PathVariable String productId,
			@RequestParam int delta) {
		CartResponse response = cartService.updateItemQuantity(userId, productId, delta);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * Removes a specific product completely from the user's active shopping cart.
	 *
	 * @param userId    The unique identifier of the user.
	 * @param productId The unique identifier of the product to remove.
	 * @return {@link ResponseEntity} containing the updated {@link CartResponse}
	 *         and HTTP status 200 OK.
	 */
	@DeleteMapping("/{userId}/items/{productId}")
	public ResponseEntity<CartResponse> removeSingleProduct(@PathVariable String userId,
			@PathVariable String productId) {
		CartResponse response = cartService.removeItemFromCart(userId, productId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * Clears all items from the user's active shopping cart.
	 *
	 * @param userId The unique identifier of the user.
	 * @return {@link ResponseEntity} containing an empty {@link CartResponse} and
	 *         HTTP status 200 OK.
	 */
	@DeleteMapping("/{userId}")
	public ResponseEntity<CartResponse> clearActiveCart(@PathVariable String userId) {
		CartResponse response = cartService.clearCart(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}