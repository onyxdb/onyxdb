package com.onyxdb.platform.configs.taskProcessing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.onyxdb.platform.processing.repositories.TaskRepository;
import com.onyxdb.platform.services.BaseClusterService;
import com.onyxdb.platform.processing.TaskProcessingWorker;
import com.onyxdb.platform.processing.consumers.CompositeTaskProcessor;

@Configuration
public class TaskProcessingConfig {
    public static final String TASK_PROCESSING_WORKER_EXECUTOR_BEAN = "taskProcessingWorkerExecutor";

    @Bean(name = TASK_PROCESSING_WORKER_EXECUTOR_BEAN)
    public ExecutorService processClusterTasksWorkerExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    @Profile("!test")
    public TaskProcessingWorker taskProcessingWorker(
            @Value("${onyxdb-app.workers.process-cluster-tasks.min-threads}")
            int minThreads,
            @Value("${onyxdb-app.workers.process-cluster-tasks.max-threads}")
            int maxThreads,
            @Value("${onyxdb-app.workers.process-cluster-tasks.polling-interval-seconds}")
            int pollingIntervalSeconds,
            CompositeTaskProcessor compositeTaskProcessor,
            BaseClusterService clusterService,
            TaskRepository taskRepository
    ) {
        return new TaskProcessingWorker(
                minThreads,
                maxThreads,
                pollingIntervalSeconds,
                compositeTaskProcessor,
                clusterService,
                taskRepository
        );
    }
}
