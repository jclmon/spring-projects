package com.jcom.product.api.dao.impl;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.jcom.product.api.dao.ProductDao;
import com.jcom.product.api.model.Product;
import com.ms.core.common.dao.impl.GenericDaoImpl;

@Repository
@CacheConfig(cacheResolver="secondaryCacheResolver")
@Cacheable(unless="#result == null")
public class ProductDaoImpl extends GenericDaoImpl<Product> implements ProductDao {
	
	public ProductDaoImpl() {
		super(Product.class);
	}
	
}
