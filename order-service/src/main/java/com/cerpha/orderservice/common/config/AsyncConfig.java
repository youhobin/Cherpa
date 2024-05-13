package com.cerpha.orderservice.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    public static final String EVENT_EXECUTOR = "EVENT_EXECUTOR";
    public static final String EVENT_EXECUTOR_NAME = "EVENT_EXECUTOR_";

    @Bean(EVENT_EXECUTOR)
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);
        executor.setMaxPoolSize(150);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix(EVENT_EXECUTOR_NAME);
        return executor;
    }
}
