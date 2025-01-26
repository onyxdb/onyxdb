package com.onyxdb.mdb.workers;


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

import com.onyxdb.mdb.models.ClusterTask;
import com.onyxdb.mdb.processors.CompositeClusterTasksProcessor;
import com.onyxdb.mdb.services.BaseClusterTaskService;

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
    private final CompositeClusterTasksProcessor clusterTasksProcessor;
    private final BaseClusterTaskService clusterTaskService;
    private final BlockingQueue<Runnable> taskQueue;

    public ProcessClusterTasksWorker(
            @Value("${onyxdb-app.workers.process-cluster-tasks.min-threads}")
            int minThreads,
            @Value("${onyxdb-app.workers.process-cluster-tasks.max-threads}")
            int maxThreads,
            @Value("${onyxdb-app.workers.process-cluster-tasks.polling-interval-seconds}")
            int pollingIntervalSeconds,
            CompositeClusterTasksProcessor clusterTasksProcessor,
            BaseClusterTaskService clusterTaskService)
    {
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
        this.pollingIntervalSeconds = pollingIntervalSeconds;
        this.clusterTasksProcessor = clusterTasksProcessor;
        this.clusterTaskService = clusterTaskService;
        this.taskQueue = new LinkedBlockingQueue<>(maxThreads);
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
        while (true) {
            int freeThreads = maxThreads - taskQueue.size();
            if (freeThreads == 0) {
                continue;
            }

            List<ClusterTask> clusterTasks = clusterTaskService.getTasksToProcess(freeThreads);
            for (var clusterTask : clusterTasks) {
                executor.execute(() -> clusterTasksProcessor.processTask(clusterTask));
            }

            Thread.sleep(pollingIntervalSeconds * 1000L);
        }
    }
}
