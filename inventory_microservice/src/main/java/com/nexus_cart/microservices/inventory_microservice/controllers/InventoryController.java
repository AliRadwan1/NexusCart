package com.nexus_cart.microservices.inventory_microservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus_cart.microservices.inventory_microservice.inventory.InventoryRepository;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	@Autowired
	private InventoryRepository inventoryRepository;
	
	// TODO Add/Create
	
	// TODO Get Stock Level by id
	
	// TODO Deduct Stock
}
