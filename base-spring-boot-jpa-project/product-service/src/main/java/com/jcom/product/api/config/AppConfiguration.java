package com.jcom.product.api.config;

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

import com.ms.core.common.async.AsyncConfig;
import com.ms.core.common.cache.CacheConfig;
import com.ms.core.common.dao.DaoConfig;
import com.ms.core.common.security.SecurityConfig;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@OpenAPIDefinition(info =
	@Info(title = "Product API", version = "1.0", description = "Documentation Product API v1.0")
)
@Import({AsyncConfig.class, DaoConfig.class, SecurityConfig.class, CacheConfig.class})
public class AppConfiguration {

	@Bean
	public ErrorAttributes errorAttributes() {
	    
		return new DefaultErrorAttributes(){
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
	 * @return
	 */
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
            	registry.addMapping("/**")
            	.allowedMethods("*");
            }
        };
	}
	
	@Bean
    public CacheManager cacheManager() {
        return CacheConfig.createCacheManager("ProductDaoImpl");
    }

	@Bean
	public Docket swaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
					.apis(RequestHandlerSelectors.basePackage("com.jcom.product.api.controller"))
					.paths(PathSelectors.any())
				.build()
				.apiInfo(new ApiInfoBuilder()
						.version("1.0")
						.title("Product API")
						.description("Documentation Product API v1.0")
						.contact(new Contact("test", "", "test@gmail.com")).build());
	}
	
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
}
