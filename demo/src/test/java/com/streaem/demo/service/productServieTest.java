package com.streaem.demo.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.streaem.demo.init.DataInitializer;
import com.streaem.demo.model.Product;

@SpringBootTest
public class productServieTest {

	@Autowired
	private ProductService productService;

	@Test
	public void testGetAllProducts_NotEmptyList() {

		List<Product> sampleProducts = Arrays.asList(new Product("ProductA", "CategoryA", 10, "any description", 0),
				new Product("ProductB", "CategoryA", 0, "any description", 0),
				new Product("ProductC", "CategoryA", 5, "any description", 5));

		try (MockedStatic<DataInitializer> mockedStatic = Mockito.mockStatic(DataInitializer.class)) {
			mockedStatic.when(DataInitializer::getProducts).thenReturn(sampleProducts);

			List<Product> result = productService.getAllProducts();

			assertEquals(sampleProducts.size(), result.size());
		}
	}

	@Test
	public void testGetAllProducts_EmptyList() {

		List<Product> emptyProducts = Collections.emptyList();

		try (MockedStatic<DataInitializer> mockedStatic = Mockito.mockStatic(DataInitializer.class)) {
			mockedStatic.when(DataInitializer::getProducts).thenReturn(emptyProducts);

			List<Product> result = productService.getAllProducts();

			assertTrue(result.isEmpty());
		}
	}

	@Test
	public void testGetProductByName_ProductFound() {

		List<Product> sampleProducts = Arrays.asList(new Product("ProductA", "CategoryA", 10, "any description", 0),
				new Product("ProductB", "CategoryA", 0, "any description", 0),
				new Product("ProductC", "CategoryA", 5, "any description", 5));

		try (MockedStatic<DataInitializer> mockedStatic = mockStatic(DataInitializer.class)) {
			mockedStatic.when(DataInitializer::getProducts).thenReturn(sampleProducts);

			String productName = "ProductB";
			Optional<Product> result = productService.getProductByName(productName);

			assertTrue(result.isPresent());
			assertEquals(productName, result.get().getName());
		}
	}

	@Test
	public void testGetProductByName_ProductNotFound() {

		List<Product> sampleProducts = Arrays.asList(new Product("ProductA", "CategoryA", 10, "any description", 0),
				new Product("ProductB", "CategoryA", 0, "any description", 0),
				new Product("ProductC", "CategoryA", 5, "any description", 5));

		try (MockedStatic<DataInitializer> mockedStatic = mockStatic(DataInitializer.class)) {
			mockedStatic.when(DataInitializer::getProducts).thenReturn(sampleProducts);

			String productName = "NonExistingProduct";
			Optional<Product> result = productService.getProductByName(productName);

			assertFalse(result.isPresent());
		}
	}

	@Test
	public void testGetProductsByCategory_AllProducts() {
		List<Product> sampleProducts = Arrays.asList(new Product("ProductA", "CategoryA", 10, "any description", 5),
				new Product("ProductB", "CategoryB", 0, "any description", 0),
				new Product("ProductC", "CategoryA", 5, "any description", 0));

		try (MockedStatic<DataInitializer> mockedStatic = mockStatic(DataInitializer.class)) {
			mockedStatic.when(DataInitializer::getProducts).thenReturn(sampleProducts);

			List<Product> result = productService.getProductsByCategory("CategoryA", null);
			assertEquals(2, result.size());
		}
	}

	@Test
	public void testGetProductsByCategory_InStock() {
		List<Product> sampleProducts = Arrays.asList(new Product("ProductA", "CategoryA", 10, "any description", 5),
				new Product("ProductB", "CategoryB", 0, "any description", 0),
				new Product("ProductC", "CategoryA", 5, "any description", 0));

		try (MockedStatic<DataInitializer> mockedStatic = mockStatic(DataInitializer.class)) {
			mockedStatic.when(DataInitializer::getProducts).thenReturn(sampleProducts);

			List<Product> result = productService.getProductsByCategory("CategoryA", true);
			assertEquals(1, result.size());
			assertEquals("ProductA", result.get(0).getName());
		}
	}

	@Test
	public void testGetProductsByCategory_NotInStock() {
		List<Product> sampleProducts = Arrays.asList(new Product("ProductA", "CategoryA", 10, "any description", 5),
				new Product("ProductB", "CategoryB", 0, "any description", 0),
				new Product("ProductC", "CategoryA", 5, "any description", 0));

		try (MockedStatic<DataInitializer> mockedStatic = mockStatic(DataInitializer.class)) {
			mockedStatic.when(DataInitializer::getProducts).thenReturn(sampleProducts);

			List<Product> result = productService.getProductsByCategory("CategoryA", false);
			assertEquals(1, result.size());
			assertEquals("ProductC", result.get(0).getName());
		}
	}

	@Test
	public void testGetProductsByCategory_NonExistingCategory() {
		List<Product> sampleProducts = Arrays.asList(new Product("ProductA", "CategoryA", 10, "any description", 5),
				new Product("ProductB", "CategoryB", 0, "any description", 0),
				new Product("ProductC", "CategoryA", 5, "any description", 0));

		try (MockedStatic<DataInitializer> mockedStatic = mockStatic(DataInitializer.class)) {
			mockedStatic.when(DataInitializer::getProducts).thenReturn(sampleProducts);

			List<Product> result = productService.getProductsByCategory("NonExistingCategory", null);
			assertEquals(0, result.size());
		}
	}

	@Test
	public void testUpdateProduct_ProductExists() {
		List<Product> sampleProducts = Arrays.asList(new Product("ProductA", "CategoryA", 10, "any description", 5),
				new Product("ProductB", "CategoryB", 0, "any description", 0),
				new Product("ProductC", "CategoryA", 5, "any description", 0));

		try (MockedStatic<DataInitializer> mockedStatic = mockStatic(DataInitializer.class)) {
			mockedStatic.when(DataInitializer::getProducts).thenReturn(sampleProducts);

			Product updatedProduct = new Product("ProductA", "NewCategory", 20, "updated description", 10);
			boolean result = productService.updateProduct(updatedProduct);
			assertTrue(result);

			Product updatedProductInList = DataInitializer.getProducts().stream()
					.filter(product -> product.getName().equalsIgnoreCase(updatedProduct.getName())).findFirst()
					.orElse(null);

			assertNotNull(updatedProductInList);
			assertEquals("NewCategory", updatedProductInList.getCategory());
			assertEquals(20, updatedProductInList.getPrice());
			assertEquals("updated description", updatedProductInList.getDescription());
			assertEquals(10, updatedProductInList.getStockLevel());
		}
	}

	@Test
	public void testUpdateProduct_ProductNotExists() {
		List<Product> sampleProducts = Arrays.asList(new Product("ProductA", "CategoryA", 10, "any description", 5),
				new Product("ProductB", "CategoryB", 0, "any description", 0),
				new Product("ProductC", "CategoryA", 5, "any description", 0));

		try (MockedStatic<DataInitializer> mockedStatic = mockStatic(DataInitializer.class)) {
			mockedStatic.when(DataInitializer::getProducts).thenReturn(sampleProducts);

			Product updatedProduct = new Product("NonExistingProduct", "NewCategory", 20, "updated description", 10);
			boolean result = productService.updateProduct(updatedProduct);
			assertFalse(result);
		}
	}

	@Test
	public void testSetStockLevel_ProductExists() {
		List<Product> sampleProducts = Arrays.asList(new Product("ProductA", "CategoryA", 10, "any description", 5),
				new Product("ProductB", "CategoryB", 0, "any description", 0),
				new Product("ProductC", "CategoryA", 5, "any description", 0));

		try (MockedStatic<DataInitializer> mockedStatic = mockStatic(DataInitializer.class)) {
			mockedStatic.when(DataInitializer::getProducts).thenReturn(sampleProducts);

			String productName = "ProductA";
			int newStockLevel = 20;
			boolean result = productService.setStockLevel(productName, newStockLevel);
			assertTrue(result);

			Product updatedProduct = DataInitializer.getProducts().stream()
					.filter(product -> product.getName().equalsIgnoreCase(productName)).findFirst().orElse(null);

			assertNotNull(updatedProduct);
			assertEquals(newStockLevel, updatedProduct.getStockLevel());
		}
	}

	@Test
	public void testSetStockLevel_ProductNotFound() {
		List<Product> sampleProducts = Arrays.asList(new Product("ProductA", "CategoryA", 10, "any description", 5),
				new Product("ProductB", "CategoryB", 0, "any description", 0),
				new Product("ProductC", "CategoryA", 5, "any description", 0));

		try (MockedStatic<DataInitializer> mockedStatic = mockStatic(DataInitializer.class)) {
			mockedStatic.when(DataInitializer::getProducts).thenReturn(sampleProducts);

			String productName = "NonExistentProduct";
			int newStockLevel = 20;
			boolean result = productService.setStockLevel(productName, newStockLevel);
			assertFalse(result);
		}
	}

}
