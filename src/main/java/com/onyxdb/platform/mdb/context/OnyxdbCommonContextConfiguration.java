package com.onyxdb.platform.mdb.context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.onyxdb.platform.mdb.operations.ConsumeTasksWorker;
import com.onyxdb.platform.mdb.operations.OperationService;
import com.onyxdb.platform.mdb.operations.consumers.CompositeTaskConsumer;

@Configuration
public class OnyxdbCommonContextConfiguration {
    public static final String CONSUME_TASKS_WORKER_EXECUTOR_BEAN = "consumeTasksWorkerExecutor";

    @Bean(name = CONSUME_TASKS_WORKER_EXECUTOR_BEAN)
    public ExecutorService processClusterTasksWorkerExecutor() {
        ThreadFactory threadFactory = new BasicThreadFactory.Builder()
                .namingPattern("consume-tasks-worker-%d")
                .build();

        return Executors.newSingleThreadExecutor(threadFactory);
    }

    @Bean
    @Profile("!test")
    public ConsumeTasksWorker taskProcessingWorker(
            @Value("${onyxdb.workers.process-cluster-tasks.min-threads}")
            int minThreads,
            @Value("${onyxdb.workers.process-cluster-tasks.max-threads}")
            int maxThreads,
            @Value("${onyxdb.workers.process-cluster-tasks.polling-interval-seconds}")
            int pollingIntervalSeconds,
            CompositeTaskConsumer compositeTaskConsumer,
            OperationService operationService
    ) {
        return new ConsumeTasksWorker(
                minThreads,
                maxThreads,
                pollingIntervalSeconds,
                compositeTaskConsumer,
                operationService
        );
    }
}
