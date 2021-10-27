package com.jcom.product.api.model;

import lombok.Data;

@Data
public class ProductDto {

	private	Long id;
	private	String name;
	private Double precio;
	private Long idSeller;
   
}