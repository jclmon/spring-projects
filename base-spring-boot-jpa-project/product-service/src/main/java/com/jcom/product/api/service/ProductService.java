package com.jcom.product.api.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jcom.product.api.model.Product;
import com.ms.core.common.service.GenericService;

import rx.Single;

public interface ProductService extends GenericService<Product> {

	Single<Product> add(Product obj, List<MultipartFile> files);

	Single<Boolean> upload(List<MultipartFile> multipartFile);
	
}
