package com.nexus_cart.microservices.Shop_microservice.products;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexus_cart.microservices.Shop_microservice.clients.InventoryClient;
import com.nexus_cart.microservices.Shop_microservice.dto.InventoryRequest;
import com.nexus_cart.microservices.Shop_microservice.dto.InventoryResponse;
import com.nexus_cart.microservices.Shop_microservice.dto.ProductRequest;
import com.nexus_cart.microservices.Shop_microservice.dto.ProductResponse;
import com.nexus_cart.microservices.Shop_microservice.exceptions.ProductAlreadyExistsException;
import com.nexus_cart.microservices.Shop_microservice.exceptions.ProductNotFoundException;

/**
 * Service layer responsible for business logic surrounding product creation,
 * catalog retrieval, filtering, and lookup operations in the Shop Microservice.
 */
@Service
public class ProductService {
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private InventoryClient inventoryClient;

	/**
	 * Creates a new product in the store catalog after verifying name uniqueness.
	 * 
	 * @param request The {@link ProductRequest} object containing product name,
	 *                category, and price.
	 * @return The newly saved product formatted as a {@link ProductResponse}.
	 * @throws ProductAlreadyExistsException If a product with the exact name
	 *                                       already exists.
	 */
	public ProductResponse createProduct(ProductRequest request) {
		if (productRepository.existsByName(request.getName())) {
			throw new ProductAlreadyExistsException("Product with name '" + request.getName() + "' already exists");
		}

		Product product = new Product(
				request.getName(), 
				request.getCategory(), 
				request.getPrice(), 
				request.getCurrency(),
				request.getImageUrls());
		Product savedProduct = productRepository.save(product);
		
		int stockToRegister = (request.getInitialStock() > 0) ? request.getInitialStock() : 0;
		int currentStock = stockToRegister;
		
		try {
			InventoryResponse invResponse = inventoryClient.createStock(new InventoryRequest(savedProduct.getId(), stockToRegister));
			if (invResponse != null && invResponse.getQuantity() != null) {
				currentStock = invResponse.getQuantity();
			}
		} catch (Exception e) {
			logger.error("Failed to register stock in Inventory Microservice for productId {}: {}", savedProduct.getId(), e.getMessage());
		}

		return mapToProductResponse(savedProduct, currentStock);
	}

	/**
	 * Fetches all products available in the store catalog.
	 * 
	 * @return A list of all products mapped to {@link ProductResponse} objects.
	 */
	public List<ProductResponse> getAllProducts() {
		List<Product> products = productRepository.findAll();
		Map<String, Integer> stockMap = fetchStockMap();

		return products.stream()
				.map(product -> mapToProductResponse(product, stockMap.getOrDefault(product.getId(), 0)))
				.collect(Collectors.toList());
	}

	/**
	 * Performs a case-insensitive search for products matching a given name query.
	 * 
	 * @param name The product name keyword to search for.
	 * @return A list of matching products mapped to {@link ProductResponse}
	 *         objects.
	 * @throws ProductNotFoundException If no products match the given search query.
	 */
	public List<ProductResponse> getProductsByName(String name) {
		List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
		if (products.isEmpty()) {
			throw new ProductNotFoundException("No products with name '" + name + "' exist");
		}

		Map<String, Integer> stockMap = fetchStockMap();

		return products.stream()
				.map(product -> mapToProductResponse(product, stockMap.getOrDefault(product.getId(), 0)))
				.collect(Collectors.toList());
	}

	/**
	 * Retrieves products filtered by a specific category.
	 * 
	 * @param category The product category string to filter by.
	 * @return A list of products within the specified category mapped to
	 *         {@link ProductResponse} objects.
	 * @throws ProductNotFoundException If no products exist in the specified
	 *                                  category.
	 */
	public List<ProductResponse> getProductsByCategory(String category) {
		List<Product> products = productRepository.findByCategoryIgnoreCase(category);
		if (products.isEmpty()) {
			throw new ProductNotFoundException("No products exist in category '" + category + "'");
		}

		Map<String, Integer> stockMap = fetchStockMap();

		return products.stream()
				.map(product -> mapToProductResponse(product, stockMap.getOrDefault(product.getId(), 0)))
				.collect(Collectors.toList());
	}

	/**
	 * Retrieves product details by its unique identifier.
	 * 
	 * @param id The unique string/UUID identifier of the product.
	 * @return The product details formatted as a {@link ProductResponse}.
	 * @throws ProductNotFoundException If no product is found with the given ID.
	 */
	public ProductResponse getProductById(String id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product with id '" + id + "' doesn't exist"));

		int quantity = 0;
		try {
			InventoryResponse inv = inventoryClient.getStockLevel(id);
			if (inv != null && inv.getQuantity() != null) {
				quantity = inv.getQuantity();
			}
		} catch (Exception e) {
			logger.error("Failed to fetch stock level for productId {}: {}", id, e.getMessage());
		}

		return mapToProductResponse(product, quantity);
	}
	
	/**
	 * Helper method to fetch stock quantities for all products in a single call.
	 */
	private Map<String, Integer> fetchStockMap() {
		try {
			List<InventoryResponse> inventoryList = inventoryClient.getAllInventory();
			if (inventoryList == null) return Collections.emptyMap();

			return inventoryList.stream()
					.filter(inv -> inv.getProductId() != null && inv.getQuantity() != null)
					.collect(Collectors.toMap(
							InventoryResponse::getProductId, 
							InventoryResponse::getQuantity, 
							(existing, replacement) -> replacement));
		} catch (Exception e) {
			logger.error("Failed to fetch inventory map from Inventory Microservice: {}", e.getMessage());
			return Collections.emptyMap();
		}
	}
	
	/**
	 * Helper mapping method constructing ProductResponse DTO with quantity.
	 */
	private ProductResponse mapToProductResponse(Product product, int quantity) {
		return new ProductResponse(
				product.getId(), 
				product.getName(), 
				product.getCategory(), 
				product.getPrice(), 
				product.getCurrency(), 
				quantity, 
				product.getImageUrls());
	}
}