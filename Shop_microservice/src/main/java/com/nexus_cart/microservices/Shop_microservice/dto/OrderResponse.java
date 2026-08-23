package com.nexus_cart.microservices.Shop_microservice.dto;

import java.math.BigDecimal;
import java.util.List;

import com.nexus_cart.microservices.Shop_microservice.orders.OrderStatus;

public class OrderResponse {
	private String id;
	private String userId;
	private OrderStatus status;
	private BigDecimal total;
	private List<OrderItemResponse> items;

	/**
	 * @param id
	 * @param userId
	 * @param status
	 * @param total
	 * @param items
	 */
	public OrderResponse(String id, String userId, OrderStatus status, BigDecimal total,
			List<OrderItemResponse> items) {
		super();
		this.id = id;
		this.userId = userId;
		this.status = status;
		this.total = total;
		this.items = items;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	/**
	 * @return the items
	 */
	public List<OrderItemResponse> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<OrderItemResponse> items) {
		this.items = items;
	}

}
