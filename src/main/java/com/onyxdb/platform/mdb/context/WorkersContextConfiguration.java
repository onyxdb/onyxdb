package com.onyxdb.platform.mdb.context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.onyxdb.platform.mdb.scheduling.tasks.ConsumeTasksWorker;
import com.onyxdb.platform.mdb.scheduling.tasks.TaskScheduler;
import com.onyxdb.platform.mdb.scheduling.tasks.consumers.CompositeTaskConsumer;

@Configuration
public class WorkersContextConfiguration {
    public static final String CONSUME_TASKS_WORKER_EXECUTOR_BEAN = "consumeTasksWorkerExecutor";

    @Bean(name = CONSUME_TASKS_WORKER_EXECUTOR_BEAN)
    public ExecutorService processClusterTasksWorkerExecutor() {
        return Executors.newSingleThreadExecutor();
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
            TaskScheduler taskScheduler
    ) {
        return new ConsumeTasksWorker(
                minThreads,
                maxThreads,
                pollingIntervalSeconds,
                compositeTaskConsumer,
                taskScheduler
        );
    }
}
