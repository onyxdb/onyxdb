package com.onyxdb.platform.processing;


import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;

import com.onyxdb.platform.context.taskProcessing.TaskProcessingContextConfiguration;
import com.onyxdb.platform.processing.consumers.CompositeTaskProcessor;
import com.onyxdb.platform.processing.models.Task;
import com.onyxdb.platform.processing.repositories.TaskRepository;
import com.onyxdb.platform.services.BaseClusterService;

/**
 * @author foxleren
 */
public class TaskProcessingWorker implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(TaskProcessingWorker.class);

    private final int minThreads;
    private final int maxThreads;
    private final int pollingIntervalSeconds;
    private final BlockingQueue<Runnable> taskQueue;
    private final CompositeTaskProcessor compositeTaskProcessor;
    private final BaseClusterService clusterService;
    private final TaskRepository taskRepository;

    public TaskProcessingWorker(
            int minThreads,
            int maxThreads,
            int pollingIntervalSeconds,
            CompositeTaskProcessor compositeTaskProcessor,
            BaseClusterService clusterService,
            TaskRepository taskRepository
    ) {
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
        this.pollingIntervalSeconds = pollingIntervalSeconds;
        this.taskQueue = new LinkedBlockingQueue<>(maxThreads);
        this.compositeTaskProcessor = compositeTaskProcessor;
        this.clusterService = clusterService;
        this.taskRepository = taskRepository;
    }

    @Override
    @Async(TaskProcessingContextConfiguration.TASK_PROCESSING_WORKER_EXECUTOR_BEAN)
    public void run(String... args) {
        try {
            logger.info("Starting processing cluster tasks");
            internalRun();
        } catch (Exception e) {
            logger.error("Failed to process cluster tasks", e);
        }
    }

    private void internalRun() {
        final var executor = new ThreadPoolExecutor(
                minThreads,
                maxThreads,
                0L,
                TimeUnit.MILLISECONDS,
                taskQueue,
                new ThreadPoolExecutor.DiscardPolicy()
        );

        try (executor) {
            processTasks(executor);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
    private void processTasks(ThreadPoolExecutor executor) throws InterruptedException {
        var isFirstIteration = true;
        while (true) {
            var scheduledAt = LocalDateTime.now();

            if (isFirstIteration) {
                isFirstIteration = false;
            } else {
                Thread.sleep(pollingIntervalSeconds * 1000L);
            }

            int freeThreads = maxThreads - taskQueue.size();
            if (freeThreads == 0) {
                logger.info("There is no free thread for processing, waiting next iteration");
                continue;
            }

            List<Task> tasks = clusterService.getTasksToProcess(freeThreads, scheduledAt);
            if (tasks.isEmpty()) {
                logger.info("There are no tasks to process, waiting next iteration");
                continue;
            }

            logger.info("Loaded {} tasks to process, adding them to executor", tasks.size());

            for (var task : tasks) {
                executor.execute(() -> compositeTaskProcessor.processTask(task));
            }

            logger.info("Finished iteration");
        }
    }
}
