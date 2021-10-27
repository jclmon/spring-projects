package com.jcom.apigateway;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * Override Original swagger implementation with custom swagger resource provider.
 */
@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {

    @Autowired
    ZuulProperties properties;
    
    @Override
    public List get() {
        List<SwaggerResource> resources = new ArrayList<>();
        properties.getRoutes().values().stream().forEach(route -> resources.add(createResource(route.getId(), "2.0")));
        return resources;
    }

    private SwaggerResource createResource(String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(location);
        swaggerResource.setLocation("/api/" + location + "/v2/api-docs");
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
    
}