package com.ms.core.common.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnMissingBean(EventConfig.class)
@Configuration
public class EventConfig {

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	@Bean
	public EventBus eventBus() {
		return new EventBus(applicationEventPublisher);
	}
	
}
