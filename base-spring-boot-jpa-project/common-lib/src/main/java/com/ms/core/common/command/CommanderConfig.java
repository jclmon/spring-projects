package com.ms.core.common.command;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@ConditionalOnMissingBean(CommanderConfig.class)
@Configuration
@EnableAsync
@EnableScheduling
public class CommanderConfig {

	@Bean
	public Commander commander() {
		return new Commander(taskExecutor(), taskScheduler());
	}

	@Bean(name="AppThreadPoolTaskExecutor")
	ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setBeanName("AppThreadPoolTaskExecutor");
		executor.setThreadNamePrefix("AppThreadPoolTaskExecutor");
		executor.initialize();
		return executor;
	}

	@Bean(name="AppThreadPoolTaskScheduler")
	ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setBeanName("AppThreadPoolTaskScheduler");
		scheduler.setThreadNamePrefix("AppThreadPoolTaskScheduler");
		scheduler.initialize();
		return scheduler;
	}

}
