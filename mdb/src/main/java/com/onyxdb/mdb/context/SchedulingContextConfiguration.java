package com.onyxdb.mdb.context;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author foxleren
 */
@Configuration
@EnableScheduling
public class SchedulingContextConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SchedulingContextConfiguration.class);

    @Bean(name = "asyncTaskExecutor")
    public Executor jobExecutor(
            @Value("${onyxdb-app.tasks.process-cluster-operations.executor.core-pool-size}")
            int corePoolSize,
            @Value("${onyxdb-app.tasks.process-cluster-operations.executor.max-pool-size}")
            int maxPoolSize,
            @Value("${onyxdb-app.tasks.process-cluster-operations.executor.queue-capacity}")
            int queueCapacity)
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("asyncTask-");
        executor.setRejectedExecutionHandler((Runnable r, ThreadPoolExecutor e) -> {
            logger.error("Task Rejected: Thread pool is full. Increase the thread pool size.");
        });
        executor.initialize();
        return executor;
    }
}
