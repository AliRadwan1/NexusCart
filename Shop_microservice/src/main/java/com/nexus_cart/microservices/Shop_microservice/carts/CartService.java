package com.nexus_cart.microservices.Shop_microservice.carts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexus_cart.microservices.Shop_microservice.dto.AddToCartRequest;
import com.nexus_cart.microservices.Shop_microservice.dto.CartItemResponse;
import com.nexus_cart.microservices.Shop_microservice.dto.CartResponse;
import com.nexus_cart.microservices.Shop_microservice.exceptions.ProductNotFoundException;
import com.nexus_cart.microservices.Shop_microservice.products.Product;
import com.nexus_cart.microservices.Shop_microservice.products.ProductRepository;

/**
 * Service layer managing shopping cart lifecycles, item management,
 * and cart total calculations for active user sessions.
 */
@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	/**
	 * Adds an item to a user's active cart or updates the quantity if it already exists.
	 *
	 * @param request The {@link AddToCartRequest} containing user ID, product ID, and quantity details.
	 * @return The updated {@link CartResponse} with recalculations for items and grand totals.
	 * @throws ProductNotFoundException If the target product does not exist in the catalog.
	 */
	@Transactional
	public CartResponse addToCart(AddToCartRequest request) {
		// 1. Retrieve or create active cart
		Cart cart = cartRepository.findByUserIdAndStatus(request.getUserId(), CartStatus.ACTIVE)
				.orElseGet(() -> new Cart(request.getUserId(), CartStatus.ACTIVE));

		// 2. Validate product exists
		Product product = productRepository.findById(request.getProductId())
				.orElseThrow(() -> new ProductNotFoundException("No product exists with id: '" + request.getProductId() + "'"));

		// 3. Find existing item in cart by matching product ID
		CartItem existingItem = cart.getItems().stream()
				.filter(item -> product.getId().equals(item.getProductId()))
				.findFirst()
				.orElse(null);

		// 4. Update quantity or attach new item
		if (existingItem != null) {
			existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
		} else {
			CartItem newItem = new CartItem(cart, product.getId(), request.getQuantity());
			cart.getItems().add(newItem);
		}

		// 5. Save and return mapped response
		Cart savedCart = cartRepository.save(cart);
		return mapToCartResponse(savedCart);
	}

	/**
	 * Retrieves the active shopping cart for a given user.
	 *
	 * @param userId The unique identifier of the user.
	 * @return The active {@link CartResponse} containing current items and totals.
	 */
	@Transactional(readOnly = true)
	public CartResponse getCartByUserId(String userId) {
		Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
				.orElseGet(() -> new Cart(userId, CartStatus.ACTIVE));

		return mapToCartResponse(cart);
	}

	/**
	 * Removes a specific product from a user's active shopping cart.
	 *
	 * @param userId The unique identifier of the user.
	 * @param productId The unique identifier of the product to remove.
	 * @return The updated {@link CartResponse} reflecting the removal.
	 */
	@Transactional
	public CartResponse removeItemFromCart(String userId, String productId) {
		Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
				.orElseGet(() -> new Cart(userId, CartStatus.ACTIVE));

		cart.getItems().removeIf(item -> productId.equals(item.getProductId()));

		Cart savedCart = cartRepository.save(cart);
		return mapToCartResponse(savedCart);
	}
	
	/**
	 * Adjusts the quantity of a product in the user's active cart by a relative delta.
	 * Positive values increase quantity; negative values decrease it.
	 * Items reaching 0 or fewer quantity are removed automatically.
	 *
	 * @param userId The unique identifier of the user.
	 * @param productId The unique identifier of the product.
	 * @param quantityDelta The relative quantity change (e.g., +1, +5, -1).
	 * @return The updated {@link CartResponse} reflecting new quantities and totals.
	 * @throws ProductNotFoundException If the item is not currently in the user's cart.
	 */
	@Transactional
	public CartResponse updateItemQuantity(String userId, String productId, int quantityDelta) {
		Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
				.orElseGet(() -> new Cart(userId, CartStatus.ACTIVE));
		
		CartItem existingItem = cart.getItems().stream()
				.filter(item -> productId.equals(item.getProductId()))
				.findFirst()
				.orElseThrow(() -> new ProductNotFoundException("Product with id '" + productId + "' is not in the cart."));
		
		int newQuantity = existingItem.getQuantity() + quantityDelta;
		
		if(newQuantity <= 0) {
			cart.getItems().remove(existingItem);
		}
		else {
			existingItem.setQuantity(newQuantity);
		}
		
		Cart savedCart = cartRepository.save(cart);
		return mapToCartResponse(savedCart);
	}

	/**
	 * Clears all items from a user's active shopping cart.
	 *
	 * @param userId The unique identifier of the user.
	 * @return An empty {@link CartResponse} for the user's active cart.
	 */
	@Transactional
	public CartResponse clearCart(String userId) {
		Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
				.orElseGet(() -> new Cart(userId, CartStatus.ACTIVE));

		cart.getItems().clear();

		Cart savedCart = cartRepository.save(cart);
		return mapToCartResponse(savedCart);
	}

	/**
	 * Maps a {@link Cart} domain entity into a formatted {@link CartResponse} DTO,
	 * resolving product metadata and computing item and grand monetary totals.
	 *
	 * @param cart The entity to map.
	 * @return The populated response DTO.
	 * @throws ProductNotFoundException If a product reference in the cart no longer exists.
	 */
	private CartResponse mapToCartResponse(Cart cart) {
		List<CartItemResponse> itemResponses = new ArrayList<>();
		BigDecimal grandTotal = BigDecimal.ZERO;

		for (CartItem item : cart.getItems()) {
			Product product = productRepository.findById(item.getProductId())
					.orElseThrow(() -> new ProductNotFoundException("No product exists with id: '" + item.getProductId() + "'"));

			BigDecimal itemTotalPrice = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
			grandTotal = grandTotal.add(itemTotalPrice);

			itemResponses.add(new CartItemResponse(
					item.getId(),
					product.getId(),
					product.getName(),
					product.getPrice(),
					item.getQuantity(),
					itemTotalPrice
			));
		}

		return new CartResponse(cart.getId(), cart.getUserId(), cart.getStatus(), itemResponses, grandTotal);
	}
}