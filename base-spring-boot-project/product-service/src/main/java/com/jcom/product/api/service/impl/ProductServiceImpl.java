package com.jcom.product.api.service.impl;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcom.product.api.dao.ProductDao;
import com.jcom.product.api.model.Product;
import com.jcom.product.api.service.ProductService;

import pl.jcom.common.security.SecurityUtil;
import pl.jcom.common.service.impl.GenericServiceImpl;
import rx.Single;

@Service
public class ProductServiceImpl extends GenericServiceImpl<Product> implements ProductService {
	
	@Autowired
	private ProductDao productDao;
	
	@PostConstruct
	void init() {
		init(Product.class, productDao);
	}
	
	@Override
	public Single<Product> add(Product product) {
		String sellerId = SecurityUtil.getAuthUserDetails().getUserId();
		product.setSellerId(sellerId);
		return super.add(product);
	}
	
	@Override
	public Single<Product> edit(Product product) {
		String sellerId = SecurityUtil.getAuthUserDetails().getUserId();
		product.setSellerId(sellerId);
		return super.edit(product);
	}
	
}
