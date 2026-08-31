package com.nexus_cart.microservices.Shop_microservice.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexus_cart.microservices.Shop_microservice.carts.Cart;
import com.nexus_cart.microservices.Shop_microservice.carts.CartItem;
import com.nexus_cart.microservices.Shop_microservice.carts.CartRepository;
import com.nexus_cart.microservices.Shop_microservice.carts.CartStatus;
import com.nexus_cart.microservices.Shop_microservice.clients.InventoryClient;
import com.nexus_cart.microservices.Shop_microservice.clients.WalletClient;
import com.nexus_cart.microservices.Shop_microservice.dto.CheckoutRequest;
import com.nexus_cart.microservices.Shop_microservice.dto.InventoryRequest;
import com.nexus_cart.microservices.Shop_microservice.dto.OrderItemResponse;
import com.nexus_cart.microservices.Shop_microservice.dto.OrderResponse;
import com.nexus_cart.microservices.Shop_microservice.dto.WalletTransactionRequest;
import com.nexus_cart.microservices.Shop_microservice.dto.WalletTransactionResponse;
import com.nexus_cart.microservices.Shop_microservice.exceptions.OrderNotFoundException;
import com.nexus_cart.microservices.Shop_microservice.exceptions.ProductNotFoundException;
import com.nexus_cart.microservices.Shop_microservice.exceptions.ServiceUnavailableException;
import com.nexus_cart.microservices.Shop_microservice.payments.Payment;
import com.nexus_cart.microservices.Shop_microservice.payments.PaymentRepository;
import com.nexus_cart.microservices.Shop_microservice.payments.PaymentStatus;
import com.nexus_cart.microservices.Shop_microservice.products.Product;
import com.nexus_cart.microservices.Shop_microservice.products.ProductRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


/**
 * Service responsible for processing order checkouts, communicating with external
 * Inventory and Wallet microservices via Feign clients, and persisting completed orders.
 */
@Service
public class OrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private InventoryClient inventoryClient;

	@Autowired
	private WalletClient walletClient;
	
	// Self-injection via @Lazy ensures Spring AOP intercepts @CircuitBreaker calls
    @Autowired
    @Lazy
    private OrderService self;

	/**
	 * Orchestrates the checkout process for an active user cart:
	 * 1. Retrieves active cart and validates item availability.
	 * 2. Deducts product inventory via the Inventory Microservice.
	 * 3. Withdraws the order total via the Wallet Microservice.
	 * 4. Persists the order and transitions the cart status to CHECKED_OUT.
	 *
	 * @param request The {@link CheckoutRequest} containing the user ID[cite: 1].
	 * @return The created {@link OrderResponse} containing item details and status[cite: 3].
	 */
	@Transactional
	public OrderResponse checkOut(CheckoutRequest request) {
		// 1. Fetch active cart
		Cart cart = cartRepository.findByUserIdAndStatus(request.getUserId(), CartStatus.ACTIVE)
				.orElseThrow(() -> new IllegalStateException("No active cart found for user: " + request.getUserId()));
		
		if (cart.getItems().isEmpty()) {
			throw new IllegalStateException("Cannot Check out with empty cart");
		}
		
		// 2. Calculate Grand Total
		BigDecimal grandTotal = BigDecimal.ZERO;
		for (CartItem item: cart.getItems()) {
			Product product = productRepository.findById(item.getProductId())
					.orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + item.getProductId()));
			
			BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
			grandTotal = grandTotal.add(itemTotal);
		}
		
		// 3. Deduct stock for each item via Feign client
		List<CartItem> deductedItems = new ArrayList<>();
	    try {
	        for (CartItem item : cart.getItems()) {
	        	self.deductStockWithCircuitBreaker(new InventoryRequest(item.getProductId(), item.getQuantity()));
	            deductedItems.add(item); // Track successfully deducted items
	        }
	    } 
	    catch (Exception e) {
	        // Rollback stock for items deducted prior to failure
	        for (CartItem item : deductedItems) {
	            try {
	                self.addStockWithCircuitBreaker(new InventoryRequest(item.getProductId(), item.getQuantity()));
	            } catch (Exception invEx) {
	                logger.error("CRITICAL ROLLBACK FAILURE: Failed to restore stock during checkout abort for productId: {}", item.getProductId(), invEx);
	            }
	        }
	        throw new IllegalStateException("Inventory deduction failed: " + e.getMessage(), e);
	    }
		
		// 4. Deduct payment from user wallet via Feign client
	    WalletTransactionResponse walletResponse;
	    try {
	        walletResponse = self.withdrawWithCircuitBreaker(new WalletTransactionRequest(request.getUserId(), grandTotal));         
	    } 
	    catch (Exception e) {
	    	// Payment failed -> Revert ALL deducted stock and abort checkout
	        for (CartItem item : cart.getItems()) {
	            try {
	                self.addStockWithCircuitBreaker(new InventoryRequest(item.getProductId(), item.getQuantity()));
	            } catch (Exception invEx) {
	                logger.error("CRITICAL ROLLBACK FAILURE: Failed to restore stock after payment failure for productId: {}", item.getProductId(), invEx);
	            }
	        }
	        throw new IllegalStateException("Payment failed: " + e.getMessage(), e);
	    }
		
	    // Save Order, Payment, and Cart entities with compensating rollback
	    try {
			// 5. Create Order entity
		    Order order = new Order(request.getUserId(), OrderStatus.COMPLETED, grandTotal);
		    for (CartItem item : cart.getItems()) {
		        OrderItem orderItem = new OrderItem(order, item.getProductId(), item.getQuantity());
		        order.addOrderItem(orderItem);
		    }
		    
		    Order savedOrder = orderRepository.save(order);
		    
		    // 6. Save Payment entity linking orderId to transactionId
		    Payment payment = new Payment(
							    		savedOrder.getId(), 
							    		walletResponse.getTransactionId(), 
							    		PaymentStatus.SUCCESSFUL
							    	);
		    paymentRepository.save(payment);
			
			// 7. Update Cart status
			cart.setStatus(CartStatus.CHECKED_OUT);
			cartRepository.save(cart);
			
			return mapToOrderResponse(savedOrder);
	    }
	    catch(Exception e) {
	    	// Step 5 Failure Compensation: Refund Wallet Funds
			try {
				self.depositWithCircuitBreaker(new WalletTransactionRequest(request.getUserId(), grandTotal));
			} 
			catch (Exception walletEx) {
				// Log critical alert if wallet refund fails
				logger.error("CRITICAL SAGA FAILURE: Failed to issue wallet refund of {} for userId {}. Manual intervention required!", 
	                    grandTotal, request.getUserId(), walletEx);
			}

			// Step 5 Failure Compensation: Revert Inventory Deductions
			for (CartItem item : cart.getItems()) {
				try {
					self.addStockWithCircuitBreaker(new InventoryRequest(item.getProductId(), item.getQuantity()));
				} catch (Exception invEx) {
					// Log critical alert if stock addition fails
					logger.error("CRITICAL SAGA FAILURE: Failed to restore stock of {} units for productId {}. Manual intervention required!", 
	                        item.getQuantity(), item.getProductId(), invEx);
				}
			}

			throw new IllegalStateException("Order completion failed after payment: " + e.getMessage(), e);
	    }
	}
	
	/**
	 * Retrieves all past orders placed by a specific user.
	 *
	 * @param userId Unique identifier of the user.
	 * @return List of {@link OrderResponse} records[cite: 3].
	 */
	@Transactional(readOnly = true)
	public List<OrderResponse> getOrdersByUserId(String userId){
		List<Order> orders = orderRepository.findByUserId(userId);
		
		return orders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());
	}
	
	/**
	 * Retrieves details of a specific order by its ID.
	 *
	 * @param orderId Unique identifier of the order.
	 * @return The {@link OrderResponse} payload[cite: 3].
	 */
	@Transactional(readOnly = true)
	public OrderResponse getOrderById(String orderId, String userId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("No order found with id: " + orderId));
		
		if (!order.getUserId().equals(userId)) {
			throw new IllegalStateException("Access denied: You do not own order " + orderId);
		}
		
		return mapToOrderResponse(order);
	}
	
	/**
	 * Cancels a specific order by its ID after validating user ownership.
	 * Triggers compensating transactions to restock inventory and refund wallet balance.
	 * 
	 * @param orderId Unique identifier of the order.
	 * @param userId  Unique identifier of the authenticated user.
	 * @return The updated {@link OrderResponse} payload[cite: 3].
	 */
	@Transactional
	public OrderResponse cancelOrderById(String orderId, String userId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("No order found with id: " + orderId));
		
		if (!order.getUserId().equals(userId)) {
			throw new IllegalStateException("Access denied: You do not own order " + orderId);
		}
		
		if (order.getStatus() == OrderStatus.CANCELLED) {
		    throw new IllegalStateException("Order " + orderId + " has already been cancelled.");
		}
		
		boolean stockRestored = true;
	    boolean refundIssued = true;
		
		// 1. Compensate Inventory (Restock)
		for(OrderItem item: order.getItems()) {
			try {
				self.addStockWithCircuitBreaker(new InventoryRequest(item.getProductId(), item.getQuantity()));
			} catch (Exception invEx) {
				stockRestored = false;
				// Log critical alert if stock addition fails
				logger.error("CRITICAL SAGA FAILURE: Failed to restore stock of {} units for productId {}. Manual intervention required!", 
                        item.getQuantity(), item.getProductId(), invEx);
			}
		}
		
		// 2. Compensate Wallet (Refund Total)
		try {
			self.depositWithCircuitBreaker(new WalletTransactionRequest(order.getUserId(), order.getTotal()));
		} 
		catch (Exception walletEx) {
			refundIssued = false;
			// Log critical alert if wallet refund fails
			logger.error("CRITICAL SAGA FAILURE: Failed to issue wallet refund of {} for userId {}. Manual intervention required!", 
                    order.getTotal(), order.getUserId(), walletEx);
		}
		
		// 3. Verify Compensation Integrity Before State Mutation
	    if (!stockRestored || !refundIssued) {
	        throw new IllegalStateException(String.format(
	            "Order cancellation incomplete for orderId %s. Stock Restored: %b, Wallet Refunded: %b. Transaction aborted.",
	            orderId, stockRestored, refundIssued
	        ));
	    }
		
		order.setStatus(OrderStatus.CANCELLED);
		Order savedOrder = orderRepository.save(order);
		
		return mapToOrderResponse(savedOrder);
	}
	
	// -----------------------------------------
	// Resilience4j Protected Wrapper Method
	// -----------------------------------------
	
	@CircuitBreaker(name = "inventoryService", fallbackMethod = "inventoryFallback")
    public void deductStockWithCircuitBreaker(InventoryRequest request) {
        inventoryClient.deductStock(request);
    }
	
	@CircuitBreaker(name = "inventoryService", fallbackMethod = "inventoryFallback")
    public void addStockWithCircuitBreaker(InventoryRequest request) {
        inventoryClient.addStock(request);
    }

    @CircuitBreaker(name = "walletService", fallbackMethod = "walletWithdrawFallback")
    public WalletTransactionResponse withdrawWithCircuitBreaker(WalletTransactionRequest request) {
        return walletClient.withdraw(request);
    }

    @CircuitBreaker(name = "walletService", fallbackMethod = "walletDepositFallback")
    public WalletTransactionResponse depositWithCircuitBreaker(WalletTransactionRequest request) {
        return walletClient.deposit(request);
    }
	
    // -----------------------------------------
    // Circuit Breaker Fallback Handlers
    // -----------------------------------------

    public void inventoryFallback(InventoryRequest request, Throwable t) {
        logger.error("Inventory Service circuit breaker triggered: {}", t.getMessage());
        throw new ServiceUnavailableException("Inventory service is temporarily unavailable. Please try again shortly.");
    }

    public WalletTransactionResponse walletWithdrawFallback(WalletTransactionRequest request, Throwable t) {
        logger.error("Wallet Service withdraw circuit breaker triggered: {}", t.getMessage());
        throw new ServiceUnavailableException("Wallet service is temporarily unavailable. Please try again shortly.");
    }

    public WalletTransactionResponse walletDepositFallback(WalletTransactionRequest request, Throwable t) {
        logger.error("Wallet Service deposit circuit breaker triggered: {}", t.getMessage());
        throw new ServiceUnavailableException("Wallet service is temporarily unavailable for refund processing.");
    }
	
	/**
	 * Helper method mapping an Order entity into an OrderResponse DTO[cite: 3].
	 */
	private OrderResponse mapToOrderResponse(Order order) {
		List<OrderItemResponse> itemResponses = new ArrayList<>();
		
		for (OrderItem item: order.getItems()) {
			Product product = productRepository.findById(item.getProductId())
					.orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + item.getProductId()));
			
			itemResponses.add(new OrderItemResponse(
							item.getId(),
							product.getId(), 
							product.getName(), 
							item.getQuantity(), 
							product.getPrice()));
		}
		
		return new OrderResponse(
						order.getId(), 
						order.getUserId(), 
						order.getStatus(), 
						order.getTotal(), 
						itemResponses);
	}
}
