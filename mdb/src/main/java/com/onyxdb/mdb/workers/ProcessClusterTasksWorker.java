package com.onyxdb.mdb.workers;


import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.onyxdb.mdb.models.ClusterTask;
import com.onyxdb.mdb.processors.CompositeClusterTasksProcessor;
import com.onyxdb.mdb.services.BaseClusterTaskService;

/**
 * @author foxleren
 */
@Component
public class ProcessClusterTasksWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ProcessClusterTasksWorker.class);

    @Value("${onyxdb-app.workers.process-cluster-tasks.min-threads}")
    private int minThreads;
    @Value("${onyxdb-app.workers.process-cluster-tasks.max-threads}")
    private int maxThreads;
    @Value("${onyxdb-app.workers.process-cluster-tasks.polling-interval-seconds}")
    private int pollingIntervalSeconds;

    private final CompositeClusterTasksProcessor processor;
    private final BaseClusterTaskService taskService;
    private final BlockingQueue<Runnable> taskQueue;

    public ProcessClusterTasksWorker(
            CompositeClusterTasksProcessor processor,
            BaseClusterTaskService taskService)
    {
        this.processor = processor;
        this.taskService = taskService;
        this.taskQueue = new LinkedBlockingQueue<>(maxThreads);
    }

    @Override
    public void run() {
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
        int pollingSize = maxThreads;
        while (true) {
            if (taskQueue.size() == pollingSize) {
                continue;
            }

            List<ClusterTask> clusterTasks = taskService.getClusterTasksToProcess(pollingSize);
            for (var clusterTask : clusterTasks) {
                executor.execute(() -> processor.processTask(clusterTask));
            }

            Thread.sleep(pollingIntervalSeconds * 1000L);
        }
    }
}
