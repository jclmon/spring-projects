package com.jcom.product.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.jcom.product.api.dao.ProductDao;
import com.jcom.product.api.model.Product;

import pl.jcom.common.dao.impl.GenericDaoImpl;

@Repository
public class ProductDaoImpl extends GenericDaoImpl<Product> implements ProductDao{
	
	public ProductDaoImpl() {
		super(Product.class);
	}
}
