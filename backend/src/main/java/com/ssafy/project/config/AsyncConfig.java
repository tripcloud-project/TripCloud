package com.ssafy.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);         // 기본 스레드 수 (동시 실행 수)
        executor.setMaxPoolSize(20);         // 최대 스레드 수
        executor.setQueueCapacity(100);      // 대기 큐 용량 (초과하면 새 스레드 생성)
        executor.setThreadNamePrefix("async-"); // 디버깅용 스레드 이름 접두사

        executor.initialize();
        return executor;
    }
}
