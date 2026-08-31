package com.nexus_cart.microservices.Shop_microservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexus_cart.microservices.Shop_microservice.dto.ProductRequest;
import com.nexus_cart.microservices.Shop_microservice.dto.ProductResponse;
import com.nexus_cart.microservices.Shop_microservice.products.ProductService;

import jakarta.validation.Valid;

/**
 * REST controller exposing endpoints for product creation, catalog retrieval,
 * search, and category filtering.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping("/create")
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
		ProductResponse response = productService.createProduct(request);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<List<ProductResponse>> retrieveAllProducts() {
		List<ProductResponse> response = productService.getAllProducts();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> retrieveProductById(@PathVariable String id) {
		ProductResponse response = productService.getProductById(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/search")
	public ResponseEntity<List<ProductResponse>> searchProductByName(@RequestParam String name) {
		List<ProductResponse> response = productService.getProductsByName(name);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/category/{category}")
	public ResponseEntity<List<ProductResponse>> retrieveCategoryProducts(@PathVariable String category) {
		List<ProductResponse> response = productService.getProductsByCategory(category);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}