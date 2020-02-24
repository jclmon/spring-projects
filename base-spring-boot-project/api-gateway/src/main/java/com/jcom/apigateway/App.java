package com.jcom.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@SpringBootApplication
@Configuration
@EnableZuulProxy
@EnableDiscoveryClient
@EnableHystrix
@EnableCircuitBreaker
@EnableAdminServer
/**
 * dashboard url: http://localhost:1111/hystrix/
 * stream url: http://localhost:8080/actuator/hystrix.stream 
 */
@EnableHystrixDashboard
@EnableTurbine
public class App {
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    }
}
