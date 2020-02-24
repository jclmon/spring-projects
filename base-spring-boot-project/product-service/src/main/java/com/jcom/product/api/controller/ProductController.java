package com.jcom.product.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcom.product.api.model.Product;
import com.jcom.product.api.service.ProductService;

import io.swagger.annotations.ApiOperation;
import pl.jcom.common.async.response.AsyncResponseEntity;
import pl.jcom.common.controller.ControllerBase;

@RestController
@RequestMapping("/products")
public class ProductController extends ControllerBase{

	@Autowired
	private ProductService productService;
	
	@RequestMapping
	@ApiOperation(value = "Get all products", response = Product.class)
    public AsyncResponseEntity<Product> getAll() {
		return makeAsyncResponse(productService.getAll());
    }
	
	@RequestMapping("/{id}")
	@ApiOperation(value = "Get product by id", response = Product.class)
    public AsyncResponseEntity<Product> getById(@PathVariable("id") String id) {
		return makeAsyncResponse(productService.getById(id));
    }
	
	@PostMapping
	@ApiOperation(value = "Add product", response = Product.class)
    public AsyncResponseEntity<Product> add(@ModelAttribute Product product) {
    	return makeAsyncResponse(productService.add(product), HttpStatus.CREATED);
    }
    
    @PutMapping
    @ApiOperation(value = "Edit product", response = Product.class)
    public AsyncResponseEntity<Product> edit(@ModelAttribute Product product) {
    	return makeAsyncResponse(productService.edit(product), HttpStatus.ACCEPTED);
    }
    
}
