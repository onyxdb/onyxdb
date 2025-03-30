package com.onyxdb.mdb.workers;


import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.onyxdb.mdb.core.clusters.processors.CompositeClusterTasksProcessor;
import com.onyxdb.mdb.core.clusters.models.ClusterTask;
import com.onyxdb.mdb.services.BaseClusterService;

/**
 * @author foxleren
 */
@Component
@Profile("!test")
public class ProcessClusterTasksWorker implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ProcessClusterTasksWorker.class);

    private final int minThreads;
    private final int maxThreads;
    private final int pollingIntervalSeconds;
    private final BlockingQueue<Runnable> taskQueue;
    private final CompositeClusterTasksProcessor clusterTasksProcessor;
    private final BaseClusterService clusterService;

    public ProcessClusterTasksWorker(
            @Value("${onyxdb-app.workers.process-cluster-tasks.min-threads}")
            int minThreads,
            @Value("${onyxdb-app.workers.process-cluster-tasks.max-threads}")
            int maxThreads,
            @Value("${onyxdb-app.workers.process-cluster-tasks.polling-interval-seconds}")
            int pollingIntervalSeconds,
            CompositeClusterTasksProcessor clusterTasksProcessor,
            BaseClusterService clusterService
    ) {
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
        this.pollingIntervalSeconds = pollingIntervalSeconds;
        this.taskQueue = new LinkedBlockingQueue<>(maxThreads);
        this.clusterTasksProcessor = clusterTasksProcessor;
        this.clusterService = clusterService;
    }

    @Override
    @Async("processClusterTasksWorkerExecutor")
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

            List<ClusterTask> clusterTasks = clusterService.getTasksToProcess(freeThreads, scheduledAt);
            if (clusterTasks.isEmpty()) {
                logger.info("There are no tasks to process, waiting next iteration");
                continue;
            }

            logger.info("Loaded {} tasks to process, adding them to executor", clusterTasks.size());

            for (var clusterTask : clusterTasks) {
                executor.execute(() -> clusterTasksProcessor.processTask(clusterTask));
            }

            logger.info("Finished iteration");
        }
    }
}
