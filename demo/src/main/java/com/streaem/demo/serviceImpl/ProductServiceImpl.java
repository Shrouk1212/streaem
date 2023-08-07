package com.streaem.demo.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.streaem.demo.init.DataInitializer;
import com.streaem.demo.model.Product;
import com.streaem.demo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Override
	public List<Product> getAllProducts() {
		return DataInitializer.getProducts();
	}

	@Override
	public Optional<Product> getProductByName(String name) {
		return DataInitializer.getProducts().stream()
				.filter(product -> product.getName() != null && product.getName().equals(name)).findFirst();
	}

	@Override
	public List<Product> getProductsByCategory(String category, Boolean inStock) {
		List<Product> filteredProducts = DataInitializer.getProducts().stream()
				.filter(product -> product.getCategory() != null && product.getCategory().equalsIgnoreCase(category))
				.collect(Collectors.toList());

		if (inStock != null && inStock) {
		    return filteredProducts.stream()
		            .filter(product -> product.getStockLevel() > 0)
		            .collect(Collectors.toList());
		} else if (inStock != null && !inStock) {
		    return filteredProducts.stream()
		            .filter(product -> product.getStockLevel() == 0)
		            .collect(Collectors.toList());
		} else {
		    return filteredProducts;
		}

	}

	@Override
	public boolean updateProduct(Product updatedProduct) {
		boolean updated = false;
		Optional<Product> productOptional = DataInitializer.getProducts().stream()
				.filter(product -> product.getName().equalsIgnoreCase(updatedProduct.getName())).findFirst();

		if (productOptional.isPresent()) {
			Product productToUpdate = productOptional.get();
			productToUpdate.setName(updatedProduct.getName());
			productToUpdate.setCategory(updatedProduct.getCategory());
			productToUpdate.setPrice(updatedProduct.getPrice());
			productToUpdate.setDescription(updatedProduct.getDescription());
			productToUpdate.setStockLevel(updatedProduct.getStockLevel());
			updated = true;
		}
		return updated;
	}

	@Override
	public boolean setStockLevel(String name, int stockLevel) {
		boolean updated = false;
		Optional<Product> productOptional = DataInitializer.getProducts().stream()
				.filter(product -> product.getName() != null && product.getName().equalsIgnoreCase(name)).findFirst();
		if (productOptional.isPresent()) {
			Product productToUpdate = productOptional.get();

			productToUpdate.setStockLevel(stockLevel);
			updated = true;
		}
		return updated;
	}

}
