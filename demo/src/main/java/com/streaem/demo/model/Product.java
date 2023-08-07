package com.streaem.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	private String name;
	private String category;
	private double price;
	private String description;
	private int stockLevel = 0;

}
