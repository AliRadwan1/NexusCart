package com.nexus_cart.microservices.Shop_microservice.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(name = "user_id")
	private String userId;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> items = new ArrayList<>();

	private BigDecimal total;

	public Order() {
		super();
	}

	/**
	 * @param userId
	 * @param status
	 * @param items
	 * @param total
	 */
	public Order(String userId, OrderStatus status, BigDecimal total) {
		super();
		this.userId = userId;
		this.status = status;
		this.total = total;
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
	 * @return the items
	 */
	public List<OrderItem> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<OrderItem> items) {
		this.items = items;
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
	 * 
	 * @param item
	 */
	public void addOrderItem(OrderItem item) {
		items.add(item);
		item.setOrder(this);
	}

}
