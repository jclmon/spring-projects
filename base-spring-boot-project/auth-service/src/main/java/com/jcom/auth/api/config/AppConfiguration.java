package com.jcom.auth.api.config;

import java.util.Map;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import pl.jcom.common.async.AsyncConfig;
import pl.jcom.common.cache.CacheConfig;
import pl.jcom.common.dao.DaoConfig;
import pl.jcom.common.sse.SseConfig;

@Configuration
@Import({ AsyncConfig.class, SseConfig.class, DaoConfig.class, CacheConfig.class })
public class AppConfiguration {

	@Bean
	public ErrorAttributes errorAttributes() {

		return new DefaultErrorAttributes() {
			@Override
			public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
				Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
				errorAttributes.remove("exception");
				return errorAttributes;
			}
		};

	}

	@Bean
	public PasswordEncoder loadPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * allow all origins - only for development mode
	 * 
	 * @return
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("*");
			}
		};
	}

	@Bean
	public CacheManager cacheManager() {
		return CacheConfig.createCacheManager("UserDaoImpl", "RoleDaoImpl", "PermissionDaoImpl");
	}

	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
