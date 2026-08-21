package com.nexus_cart.microservices.Shop_microservice.products;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	@Autowired
	private ProductRepository productRepository;

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

		Product product = new Product(request.getName(), request.getCategory(), request.getPrice());
		productRepository.save(product);

		return new ProductResponse(product.getId(), product.getName(), product.getCategory(), product.getPrice());
	}

	/**
	 * Fetches all products available in the store catalog.
	 * 
	 * @return A list of all products mapped to {@link ProductResponse} objects.
	 */
	public List<ProductResponse> getAllProducts() {
		return productRepository.findAll().stream().map(product -> new ProductResponse(product.getId(),
				product.getName(), product.getCategory(), product.getPrice())).collect(Collectors.toList());
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

		return products.stream().map(product -> new ProductResponse(product.getId(), product.getName(),
				product.getCategory(), product.getPrice())).collect(Collectors.toList());
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

		return products.stream().map(product -> new ProductResponse(product.getId(), product.getName(),
				product.getCategory(), product.getPrice())).collect(Collectors.toList());
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

		return new ProductResponse(product.getId(), product.getName(), product.getCategory(), product.getPrice());
	}
}