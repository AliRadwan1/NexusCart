package com.nexus_cart.microservices.Shop_microservice.dto;

import com.nexus_cart.microservices.Shop_microservice.orders.OrderStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateOrderStatusRequest {
	@NotBlank(message = "Order ID cannot be blank")
    private String orderId;

    @NotNull(message = "Order status cannot be null")
    private OrderStatus status;

	/**
	 * @param orderId
	 * @param status
	 */
	public UpdateOrderStatusRequest(@NotBlank String orderId, @NotBlank OrderStatus status) {
		super();
		this.orderId = orderId;
		this.status = status;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the status
	 */
	public OrderStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(OrderStatus status) {
		this.status = status;
	}

}
