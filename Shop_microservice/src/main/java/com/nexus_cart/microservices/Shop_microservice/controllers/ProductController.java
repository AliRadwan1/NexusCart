package com.nexus_cart.microservices.Shop_microservice.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexus_cart.microservices.Shop_microservice.dto.DeleteProductRequest;
import com.nexus_cart.microservices.Shop_microservice.dto.ProductRequest;
import com.nexus_cart.microservices.Shop_microservice.dto.ProductResponse;
import com.nexus_cart.microservices.Shop_microservice.dto.UpdateProductRequest;
import com.nexus_cart.microservices.Shop_microservice.products.ProductService;

import jakarta.validation.Valid;

/**
 * REST controller exposing endpoints for product creation, catalog retrieval,
 * search, category filtering, updating, and deletion in the Shop Microservice.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Creates a new product in the store catalog. Restrictable to administrative roles.
     *
     * @param request Validated {@link ProductRequest} body.
     * @return {@link ResponseEntity} containing the created {@link ProductResponse} and 201 Created status.
     */
    @PostMapping("/create")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves all products available in the store catalog along with stock levels.
     *
     * @return {@link ResponseEntity} containing a list of {@link ProductResponse} and 200 OK status.
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> retrieveAllProducts() {
        List<ProductResponse> response = productService.getAllProducts();
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Retrieves details for a specific product by its ID.
     *
     * @param id Product unique identifier path variable.
     * @return {@link ResponseEntity} containing {@link ProductResponse} and 200 OK status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> retrieveProductById(@PathVariable String id) {
        ProductResponse response = productService.getProductById(id);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Performs a keyword search for products by name.
     *
     * @param name Name query search parameter.
     * @return {@link ResponseEntity} containing matched {@link ProductResponse} items and 200 OK status.
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProductByName(@RequestParam String name) {
        List<ProductResponse> response = productService.getProductsByName(name);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Filters and returns products belonging to a specific category.
     *
     * @param category Category path variable.
     * @return {@link ResponseEntity} containing categorized {@link ProductResponse} items and 200 OK status.
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> retrieveCategoryProducts(@PathVariable String category) {
        List<ProductResponse> response = productService.getProductsByCategory(category);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    /**
     * Updates existing product metadata. Admin only.
     *
     * @param request Validated {@link UpdateProductRequest} body.
     * @return {@link ResponseEntity} containing updated {@link ProductResponse} and 200 OK status.
     */
    @PutMapping("/update")
    public ResponseEntity<ProductResponse> updateProductById(@Valid @RequestBody UpdateProductRequest request){
        ProductResponse response = productService.updateProduct(request);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    /**
     * Deletes a product from the catalog by ID. Admin only.
     *
     * @param request Validated {@link DeleteProductRequest} body containing product ID.
     * @return {@link ResponseEntity} with confirmation message map and 200 OK status.
     */
    @PostMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteProductById(@Valid @RequestBody DeleteProductRequest request){
        productService.deleteProduct(request);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Product deleted successfully"));
    }
}