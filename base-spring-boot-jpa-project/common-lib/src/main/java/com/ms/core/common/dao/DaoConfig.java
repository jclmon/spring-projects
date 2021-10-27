package com.ms.core.common.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ms.core.common.dao.transaction.ObservableTxFactory;

@Configuration
@EnableTransactionManagement
public class DaoConfig {
	
	@Bean
	public ObservableTxFactory observableTxFactory() {
	    return new ObservableTxFactory();
	}
	
}