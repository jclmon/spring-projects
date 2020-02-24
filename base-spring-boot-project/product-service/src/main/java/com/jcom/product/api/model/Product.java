package com.jcom.product.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pl.jcom.common.model.EntityBase;

@Document(collection = "Product")
@ApiModel(description = "Product data")
public class Product extends EntityBase{

	@ApiModelProperty(notes = "Seller id")
	private String sellerId;
	@ApiModelProperty(notes = "Product name")
	private String name;
	@ApiModelProperty(notes = "Product price")
	private double price;
	
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
