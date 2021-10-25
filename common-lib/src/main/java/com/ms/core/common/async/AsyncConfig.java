package com.ms.core.common.async;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ms.core.common.async.handlers.AsyncResponseEntityReturnHandler;

@Configuration
public class AsyncConfig {

	@Bean
	public AsyncResponseEntityReturnHandler asyncResponseEntityHandler() {
        return new AsyncResponseEntityReturnHandler();
    }
	
	@Bean
    public WebMvcConfigurer rxJavaWebMvcConfiguration(List<AsyncHandlerMethodReturnValueHandler> handlers) {
        return new XGWebMvcConfigurerAdapter() {
            @Override
            public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
                if (handlers != null) {
                    returnValueHandlers.addAll(handlers);
                }
            }
        };
    }
	
	
	/**
	 * inner class
	 */
	class XGWebMvcConfigurerAdapter implements WebMvcConfigurer {}
		
}
