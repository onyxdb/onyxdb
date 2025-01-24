package com.onyxdb.mdb.context;

import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.onyxdb.mdb.generators.ClusterTasksGenerator;
import com.onyxdb.mdb.generators.CompositeClusterTasksGenerator;
import com.onyxdb.mdb.generators.MongoClusterTasksGenerator;
import com.onyxdb.mdb.processors.ClusterOperationProcessor;
import com.onyxdb.mdb.processors.CompositeClusterOperationProcessor;
import com.onyxdb.mdb.processors.MongoClusterOperationProcessor;

/**
 * @author foxleren
 */
@Configuration
@EnableScheduling
public class MdbContextConfiguration {
    @Bean
    public CompositeClusterTasksGenerator compositeClusterTasksGenerator(
            MongoClusterTasksGenerator mongoClusterTasksGenerator)
    {
        List<ClusterTasksGenerator> generators = List.of(
                mongoClusterTasksGenerator
        );
        return new CompositeClusterTasksGenerator(generators);
    }

    @Bean
    public CompositeClusterOperationProcessor compositeClusterOperationProcessor(
            MongoClusterOperationProcessor mongoClusterOperationProcessor)
    {
        List<ClusterOperationProcessor> processors = List.of(
                mongoClusterOperationProcessor
        );
        return new CompositeClusterOperationProcessor(processors);
    }

    @Bean(name = "processClusterOperationTaskExecutor")
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
//        executor.setPo
        executor.setThreadNamePrefix("processClusterOperationTaskExecutor-");
        executor.initialize();
        return executor;
    }
}
