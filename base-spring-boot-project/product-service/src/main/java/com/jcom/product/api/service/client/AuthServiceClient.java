package com.jcom.product.api.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

/**
 * Feign client example
 */
@FeignClient(value = "auth-service", fallbackFactory = AuthFallbackFactory.class)
public interface AuthServiceClient {

	@RequestMapping("current")
	public UserDetails getCurrent();
	
}

@Component
class AuthFallbackFactory implements FallbackFactory<AuthServiceClient> {

	@Override
	public AuthServiceClient create(Throwable cause) {
		return new AuthFallbackService(cause);
	}

}

class AuthFallbackService implements AuthServiceClient {

	private Logger logger = LoggerFactory.getLogger(AuthFallbackService.class);

	private final Throwable cause;

	public AuthFallbackService(Throwable cause) {
		this.cause = cause;
	}

	@Override
	public UserDetails getCurrent() {
		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			logger.error(String.format("404 error took place when access service with info: %s", cause.getLocalizedMessage()));
		} else {
			logger.error("Other error took place: " + cause.getLocalizedMessage());
		}
		return null;
	}

}