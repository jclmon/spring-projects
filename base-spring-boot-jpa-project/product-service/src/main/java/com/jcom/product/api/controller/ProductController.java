package com.jcom.product.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jcom.product.api.model.Product;
import com.jcom.product.api.model.ProductDto;
import com.jcom.product.api.service.AlertServiceClient;
import com.jcom.product.api.service.ProductService;
import com.ms.core.common.async.response.AsyncResponseEntity;
import com.ms.core.common.controller.ControllerBase;
import com.ms.core.common.exception.ResourceNotFoundException;
import com.ms.core.common.model.PaginatedResponse;

import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import rx.exceptions.Exceptions;

@RestController
public class ProductController extends ControllerBase {

	@Autowired
	private ProductService productService;

	@Autowired
	private AlertServiceClient alertServiceClient;

	@GetMapping("/products")
	@ApiOperation(value = "Get all products", response = PaginatedResponse.class)
	public AsyncResponseEntity<PaginatedResponse> getAll(@RequestParam("sort") String[] sort, @RequestParam("page") int page, @RequestParam("size") int size) {
		return makeAsyncResponse(productService.getAll(sort, page, size));
	}
	
	@GetMapping("/products/search/filter")
	public AsyncResponseEntity<PaginatedResponse> getByFilter (
			@RequestParam("column") String column, @RequestParam("query") String query,
			@RequestParam("sort") String[] sort, @RequestParam("page") int page, @RequestParam("size") int size) {
		return makeAsyncResponse(productService.getByQuery(column, query, sort, page, size));				
	}
	
	@GetMapping("/products/{id}")
	@ApiOperation(value = "Get product by id", response = Product.class)
	public AsyncResponseEntity<Product> getById(@PathVariable("id") Long id) {
		return makeAsyncResponse(productService.getById(id));
	}

	@PostMapping("/products/upload")
	public AsyncResponseEntity<Boolean> add(@RequestParam("document") MultipartFile multipartFile) {
		List<MultipartFile> array = new ArrayList<MultipartFile>();
		array.add(multipartFile);
		return makeAsyncResponse(productService.upload(array), null, HttpStatus.CREATED);		
	}

	@PostMapping("/products")
	@ApiOperation(value = "Add product", response = Product.class)
	public AsyncResponseEntity<Product> add(@RequestHeader("Authorization") String auth, @RequestPart("product") Product product, @RequestPart("files") List<MultipartFile> files) {
		return makeAsyncResponse(productService.add(product, files).map(obj -> {
        	if(obj == null)
        		throw Exceptions.propagate(new ResourceNotFoundException());
        	
			MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		    mapperFactory.classMap(Product.class, ProductDto.class);
		    MapperFacade mapper = mapperFactory.getMapperFacade();
		    ProductDto dto = mapper.map(obj, ProductDto.class);
			alertServiceClient.addAlertProduct(auth, dto);
			
        	return obj;
        }), HttpStatus.CREATED);
	}

	@PutMapping("/products")
	@ApiOperation(value = "Edit product", response = Product.class)
	public AsyncResponseEntity<Product> edit(@RequestBody Product product) {
		return makeAsyncResponse(productService.edit(product), HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/products")
	@ApiOperation(value = "Delete product", response = Product.class)
	public AsyncResponseEntity<Product> delete(@RequestBody Product product) {
		return makeAsyncResponse(productService.delete(product));
	}
	
}
