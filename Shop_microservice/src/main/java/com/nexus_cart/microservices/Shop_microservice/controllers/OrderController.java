package com.nexus_cart.microservices.Shop_microservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus_cart.microservices.Shop_microservice.dto.CheckoutRequest;
import com.nexus_cart.microservices.Shop_microservice.dto.OrderCancelRequest;
import com.nexus_cart.microservices.Shop_microservice.dto.OrderResponse;
import com.nexus_cart.microservices.Shop_microservice.dto.UpdateOrderStatusRequest;
import com.nexus_cart.microservices.Shop_microservice.orders.OrderService;

import jakarta.validation.Valid;

/**
 * REST controller exposing endpoints for order processing, order retrieval by user or ID,
 * and order cancellation with automated inventory and wallet compensation.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	/**
	 * Helper method to retrieve userId from JWT SecurityContext.
	 */
	private String getAuthenticatedUserId(Authentication authentication) {
		return (String) authentication.getPrincipal();
	}

	/**
	 * Processes the checkout workflow for a user's active shopping cart,
	 * orchestrating inventory deduction and wallet balance withdrawal.
	 *
	 * @param request Validated {@link CheckoutRequest} containing the user ID executing checkout.
	 * @return {@link ResponseEntity} containing the created {@link OrderResponse} and HTTP status 201 CREATED.
	 */
	@PostMapping("/checkout")
	public ResponseEntity<OrderResponse> checkout(Authentication authentication, 
			@Valid @RequestBody CheckoutRequest request) {
		String userId = getAuthenticatedUserId(authentication);
		request.setUserId(userId);
		
		OrderResponse response = orderService.checkOut(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * Retrieves all historical orders placed by a specific user.
	 *
	 * @param userId Unique identifier of the target user.
	 * @return {@link ResponseEntity} containing a list of {@link OrderResponse} objects and HTTP status 200 OK.
	 */
	@GetMapping
	public ResponseEntity<List<OrderResponse>> retrieveUserOrders(Authentication authentication) {
		String userId = getAuthenticatedUserId(authentication);
		List<OrderResponse> response = orderService.getOrdersByUserId(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * Retrieves detailed information for a specific order by its unique ID.
	 *
	 * @param orderId Unique identifier of the requested order.
	 * @return {@link ResponseEntity} containing the matching {@link OrderResponse} and HTTP status 200 OK.
	 */
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponse> retrieveOrderById(Authentication authentication, 
			@PathVariable String orderId) {
		String userId = getAuthenticatedUserId(authentication);
		OrderResponse response = orderService.getOrderById(orderId, userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * Cancels an active order, triggering inventory replenishment and user wallet refund calls via Feign.
	 *
	 * @param orderId Unique identifier of the order to cancel.
	 * @return {@link ResponseEntity} containing the updated {@link OrderResponse} with status CANCELLED and HTTP status 200 OK.
	 */
	@PutMapping("/cancel")
	public ResponseEntity<OrderResponse> cancelOrder(Authentication authentication, 
			@Valid @RequestBody OrderCancelRequest request) {
		String userId = getAuthenticatedUserId(authentication);
		OrderResponse response = orderService.cancelOrderById(request.getOrderId(), userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	/**
     * Retrieves all platform customer orders. Admin only.
     * 
     * @return {@link ResponseEntity} containing a list of all {@link OrderResponse} records and 200 OK.
     */
    @GetMapping("/admin/all")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrdersForAdmin();
        
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Manually updates the status of a specific order. Admin only.
     * 
     * @param request Validated {@link UpdateOrderStatusRequest} body.
     * @return {@link ResponseEntity} containing the updated {@link OrderResponse} and 200 OK.
     */
    @PutMapping("/admin/update")
    public ResponseEntity<OrderResponse> updateOrderStatus(@Valid @RequestBody UpdateOrderStatusRequest request) {
        OrderResponse updatedOrder = orderService.updateOrderStatusByAdmin(request);
        
        return ResponseEntity.ok(updatedOrder);
    }
}