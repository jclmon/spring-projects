package com.ms.core.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

	@Bean
	public CommonAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new CommonAuthenticationEntryPoint();
    }
	
}
