package com.streaem.demo.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.streaem.demo.model.Product;
import com.streaem.demo.service.ProductService;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Test
	public void testGetAllProducts_success() {
		List<Product> products = new ArrayList<>();
		when(productService.getAllProducts()).thenReturn(products);
		try {
			mockMvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON)).andDo(result -> {

				Assertions.assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testGetProductByName_success() {
		Product product = new Product();
		product.setName("Sample Product");
		when(productService.getProductByName("Sample Product")).thenReturn(Optional.of(product));
		try {
			mockMvc.perform(get("/products/{name}", "Sample Product").contentType(MediaType.APPLICATION_JSON))
					.andDo(result -> {

						Assertions.assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
						Assertions.assertEquals(product.getName(), "Sample Product");

					});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testGetProductByName_NotFound() throws Exception {

		when(productService.getProductByName("sample")).thenReturn(Optional.empty());

		mockMvc.perform(get("/products/{name}", "Non-existent Product")).andExpect(status().isNotFound());
	}

	@Test
	public void testGetProductsByCategory_Success() {
		String category = "CategoryA";
		Boolean inStock = null;

		List<Product> mockProducts = Arrays.asList(new Product("ProductA", "CategoryA", 10, "any description", 0),
				new Product("ProductB", "CategoryA", 0, "any description", 0),
				new Product("ProductC", "CategoryA", 5, "any description", 5));
		when(productService.getProductsByCategory(category, inStock)).thenReturn(mockProducts);

		try {
			mockMvc.perform(get("/products/category/{category}", category)).andExpect(status().isOk())
					.andExpect(jsonPath("$.length()").value(3));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}





	@Test
	public void testGetProductsByCategory_NotFound() {
		String category = "CategoryA";
		Boolean inStock = false;

		List<Product> mockProducts = new ArrayList<>();
		when(productService.getProductsByCategory(category, inStock)).thenReturn(mockProducts);

		try {
			mockMvc.perform(get("/products/category/{category}", category)).andExpect(status().isNotFound());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateProduct_Success() throws Exception {

		Product updatedProduct = new Product();
		updatedProduct.setName("Ergonomic Granite Keyboard");
		updatedProduct.setCategory("Games");
		updatedProduct.setPrice(641.00);
		updatedProduct.setDescription(
				"The slim & simple Maple Gaming Keyboard from Dev Byte comes with a sleek body and 7-Color RGB LED Back-lighting for smart functionality");

		when(productService.updateProduct(eq(updatedProduct))).thenReturn(true);

		String jsonProduct = "{ \"name\":\"Ergonomic Granite Keyboard\", \"category\":\"Games\", \"price\":\"641.00\", \"description\":\"The slim & simple Maple Gaming Keyboard from Dev Byte comes with a sleek body and 7-Color RGB LED Back-lighting for smart functionality\"}";

		mockMvc.perform(post("/products/update").contentType(MediaType.APPLICATION_JSON).content(jsonProduct))
				.andExpect(status().isOk());

		verify(productService, times(1)).updateProduct(updatedProduct);
	}

	@Test
	public void testUpdateProduct_NotFound() throws Exception {

		Product updatedProduct = new Product();
		updatedProduct.setName("Non-existent Product");
		updatedProduct.setCategory("Non-existent Category");
		updatedProduct.setPrice(100.00);
		updatedProduct.setDescription("This product does not exist.");

		when(productService.updateProduct(eq(updatedProduct))).thenReturn(false);

		String jsonProduct = "{ \"name\":\"Non-existent Product\", \"category\":\"Non-existent Category\", \"price\":\"100.00\", \"description\":\"This product does not exist.\"}";

		mockMvc.perform(post("/products/update").contentType(MediaType.APPLICATION_JSON).content(jsonProduct))
				.andExpect(status().isNotFound());

		verify(productService, times(1)).updateProduct(eq(updatedProduct));
	}

	@Test
	public void testSetStockLevel_Success() throws Exception {
		String productName = "Sample Product";
		int stockLevel = 50;

		when(productService.setStockLevel(eq(productName), eq(stockLevel))).thenReturn(true);

		mockMvc.perform(put("/products/{name}/stock", productName).param("stockLevel", String.valueOf(stockLevel)))
				.andExpect(status().isOk());

		verify(productService, times(1)).setStockLevel(eq(productName), eq(stockLevel));
	}

	@Test
	public void testSetStockLevel_NotFound() throws Exception {
		String productName = "Non-existent Product";
		int stockLevel = 100;

		when(productService.setStockLevel(eq(productName), eq(stockLevel))).thenReturn(false);

		mockMvc.perform(put("/products/{name}/stock", productName).param("stockLevel", String.valueOf(stockLevel)))
				.andExpect(status().isNotFound());

		verify(productService, times(1)).setStockLevel(eq(productName), eq(stockLevel));
	}

}
