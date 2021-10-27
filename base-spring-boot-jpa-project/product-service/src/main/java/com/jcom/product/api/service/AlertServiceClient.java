package com.jcom.product.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties.FeignClientConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.jcom.product.api.model.ProductDto;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

@FeignClient(value = "alert-service", configuration = FeignClientConfiguration.class, fallbackFactory = AlertFallbackFactory.class)
public interface AlertServiceClient {

	// http://localhost:8080/api/alert-service/alert/product
	@PostMapping("/alert/product")
	public Boolean addAlertProduct(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @RequestBody ProductDto producto);

	// http://localhost:8080/api/alert-service/alert/product
	@DeleteMapping("/alert/product")
	public Boolean deleteAlertProduct(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @RequestBody ProductDto producto);

}

@Component
class AlertFallbackFactory implements FallbackFactory<AlertServiceClient> {

	@Override
	public AlertServiceClient create(Throwable cause) {
		return new AlertFallbackService(cause);
	}

}

class AlertFallbackService implements AlertServiceClient {

	private Logger logger = LoggerFactory.getLogger(AlertFallbackService.class);

	private final Throwable cause;

	public AlertFallbackService(Throwable cause) {
		this.cause = cause;
	}

	@Override
	public Boolean addAlertProduct(String authorizationHeader, ProductDto producto) {
		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			logger.error(String.format("404 error took place when access service with info: %s Error message: %s", producto.getId(), cause.getLocalizedMessage()));
		} else {
			logger.error("Other error took place: " + cause.getLocalizedMessage());
		}
		return null;
	}

	@Override
	public Boolean deleteAlertProduct(String authorizationHeader, ProductDto producto) {
		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			logger.error(String.format("404 error took place when access service with info: %s Error message: %s", producto.getId(), cause.getLocalizedMessage()));
		} else {
			logger.error("Other error took place: " + cause.getLocalizedMessage());
		}
		return null;
	}

}