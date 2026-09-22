package com.promptduel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Application-wide configuration beans.
 */
@Configuration
public class AppConfig {

    /**
     * Fixed thread pool for parallel LLM evaluation calls.
     * Size is configurable via LLM_THREAD_POOL_SIZE env var (default: 5).
     */
    @Bean(name = "llmExecutor", destroyMethod = "shutdown")
    public ExecutorService llmExecutor(
            @Value("${app.llm.thread-pool-size}") int poolSize) {
        return Executors.newFixedThreadPool(poolSize);
    }
}
