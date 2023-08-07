package com.streaem.demo.service;

import java.util.List;
import java.util.Optional;

import com.streaem.demo.model.Product;

public interface ProductService {

	List<Product> getAllProducts();

	Optional<Product> getProductByName(String name);

	List<Product> getProductsByCategory(String category, Boolean inStock);

	boolean updateProduct(Product updatedProduct);

	boolean setStockLevel(String name, int stockLevel);

}
