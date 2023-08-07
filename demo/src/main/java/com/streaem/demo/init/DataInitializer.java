package com.streaem.demo.init;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.streaem.demo.model.Product;

@Component
public class DataInitializer {
	
	private final String EXTERNAL_API_URL = "http://localhost:4001/productdata";
	  private final WebClient webClient = WebClient.create(EXTERNAL_API_URL);
	  
	  private static List<Product> products = new ArrayList<>();
	  
	  
	
	public static List<Product> getProducts() {
		return products;
	}



	public static void setProducts(List<Product> products) {
		DataInitializer.products = products;
	}



	@PostConstruct
	public void initializeDataModel() {
		products = webClient.get()
              .retrieve()
              .bodyToFlux(Product.class)
              .collectList()
              .block(); 

	}

}
