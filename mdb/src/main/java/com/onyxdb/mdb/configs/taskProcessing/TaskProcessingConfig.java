package com.onyxdb.mdb.configs.taskProcessing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.services.BaseClusterService;
import com.onyxdb.mdb.taskProcessing.TaskProcessingWorker;
import com.onyxdb.mdb.taskProcessing.processors.CompositeTaskProcessor;

@Configuration
public class TaskProcessingConfig {
    public static final String TASK_PROCESSING_WORKER_EXECUTOR_BEAN = "taskProcessingWorkerExecutor";

    @Bean(name = TASK_PROCESSING_WORKER_EXECUTOR_BEAN)
    public ExecutorService processClusterTasksWorkerExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public TaskProcessingWorker taskProcessingWorker(
            @Value("${onyxdb-app.workers.process-cluster-tasks.min-threads}")
            int minThreads,
            @Value("${onyxdb-app.workers.process-cluster-tasks.max-threads}")
            int maxThreads,
            @Value("${onyxdb-app.workers.process-cluster-tasks.polling-interval-seconds}")
            int pollingIntervalSeconds,
            CompositeTaskProcessor compositeTaskProcessor,
            BaseClusterService clusterService
    ) {
        return new TaskProcessingWorker(
                minThreads,
                maxThreads,
                pollingIntervalSeconds,
                compositeTaskProcessor,
                clusterService
        );
    }
}
