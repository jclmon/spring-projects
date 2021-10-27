package com.jcom.product.api.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jcom.product.api.dao.ProductDao;
import com.jcom.product.api.model.Product;
import com.jcom.product.api.service.AlertServiceClient;
import com.jcom.product.api.service.ProductService;
import com.ms.core.common.constants.ConfigConstants;
import com.ms.core.common.exception.DataAccessException;
import com.ms.core.common.security.SecurityUtil;
import com.ms.core.common.service.impl.GenericServiceImpl;

import rx.Single;
import rx.exceptions.Exceptions;

@Service
public class ProductServiceImpl extends GenericServiceImpl<Product> implements ProductService {
	
	@Value(ConfigConstants.IMAGE_UPLOAD_LOCATION)
	private String IMAGE_LOCATION;

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private AlertServiceClient alertServiceClient;
	
//	@Autowired
//	private ProductQueryRepository productQueryRepository;
	
	@PostConstruct
	void init() {
		init(Product.class, productDao);
	}
	
	@Override
	public Single<Boolean> upload(List<MultipartFile> multipartFile) {
		try {
		    for(MultipartFile mf: multipartFile) {
                byte[] bytes = mf.getBytes();
                Path path = Paths.get(IMAGE_LOCATION + mf.getOriginalFilename());
                Files.write(path, bytes);
            }
	        return Single.just(Boolean.TRUE);

	    } catch (IOException e) {
	    	throw Exceptions.propagate(new DataAccessException(e));

	    }

	}
	
	@Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, 
				rollbackFor = DataAccessException.class)
	public Single<Product> add(Product obj, List<MultipartFile> files) {
		try {
			Long sellerId = SecurityUtil.getAuthUserDetails().getUserId();
			obj.setSellerid(sellerId);
			Product product = productDao.add(obj);
			if(files != null) {
			    for(MultipartFile mf: files) {
	                byte[] bytes = mf.getBytes();
	                Path path = Paths.get(IMAGE_LOCATION + product.getId() + "_" + mf.getOriginalFilename());
	                Files.write(path, bytes);
	            }
			}
			
	        return Single.just(product);
	        
	    } catch (DataAccessException de) {
	    	throw Exceptions.propagate(de);
	    	
	    } catch (IOException e) {
	    	throw Exceptions.propagate(new DataAccessException(e));

	    } catch (Exception e) {
	    	throw Exceptions.propagate(new DataAccessException(e));

	    }

	}
	
	@Override
	public Single<Product> edit(Product product) {
		Long sellerId = SecurityUtil.getAuthUserDetails().getUserId();
		product.setSellerid(sellerId);
		return super.edit(product);
	}
	
}
