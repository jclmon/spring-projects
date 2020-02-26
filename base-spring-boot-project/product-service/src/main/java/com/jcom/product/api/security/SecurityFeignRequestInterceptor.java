package com.jcom.product.api.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/** 
 * Comunication interceptor for Feing, add token to header
 *
 */
public class SecurityFeignRequestInterceptor implements RequestInterceptor {
 
	private static Logger logger = LoggerFactory.getLogger(SecurityFeignRequestInterceptor.class);
	
	@Value("${jwt.header}")
	private String AUTHENTICATION_HEADER;
 
	@Override
    public void apply(RequestTemplate template) {
        propagateAuthorizationHeader(template);
	}
 
	private void propagateAuthorizationHeader(RequestTemplate template) {
		if (template.headers().containsKey(AUTHENTICATION_HEADER)) {
			logger.trace("the authorization {} token has been already set", AUTHENTICATION_HEADER);
        } else {
        	logger.trace("setting the authorization token {}", AUTHENTICATION_HEADER);
        	template.header(AUTHENTICATION_HEADER, (String) SecurityContextHolder.getContext().getAuthentication().getCredentials());
        }
	}
	
}