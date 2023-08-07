package com.streaem.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.streaem.demo.init.DataInitializer;
import com.streaem.demo.model.Product;
import com.streaem.demo.service.ProductService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	ProductService productService;

	/**
	 *  Get all products.
	 * 
	 *  @return ResponseEntity containing list of products
	*/
	@GetMapping
	public ResponseEntity<List<Product>> getAllProducts() {
		return ResponseEntity.ok(productService.getAllProducts());
	}

	/**
	 *  Get product by name.
	 * 
	 * @param name The name of the product
	 * @return ResponseEntity containing the product
	*/
	@GetMapping("/{name}")
	public ResponseEntity<Product> getProductByName(@PathVariable String name) {
		return productService.getProductByName(name).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	/**
	 *  Get Products by category and can be filtered by instock.
	 * 
	 * @param category 
	 * @param instok 
	 * @return ResponseEntity containing successful message or an error
	*/
	@GetMapping("/category/{category}")
	public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category,
			@RequestParam(required = false) Boolean inStock) {

		List<Product> filteredProducts = productService.getProductsByCategory(category, inStock);

		if (filteredProducts.size() > 0) {
			return ResponseEntity.ok(filteredProducts);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 *  update any field of a product.
	 * 
	 * @param updatedProduct The new object
	*/
	@PostMapping("/update")
	public ResponseEntity<Void> updateProduct(@RequestBody Product updatedProduct) {
		if (productService.updateProduct(updatedProduct)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	/**
	 * set stock level.
	 * 
	 * @param name of the product
	 * @param stockLevel the stock level
	*/
	@PutMapping("/{name}/stock")
	public ResponseEntity<Void> setStockLevel(@PathVariable String name, @RequestParam int stockLevel) {
		if (productService.setStockLevel(name, stockLevel)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
