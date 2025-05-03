package com.onyxdb.platform.mdb.scheduling.tasks;


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

import com.onyxdb.platform.mdb.context.WorkersContextConfiguration;
import com.onyxdb.platform.mdb.scheduling.tasks.consumers.CompositeTaskConsumer;
import com.onyxdb.platform.mdb.scheduling.tasks.models.Task;

/**
 * @author foxleren
 */
public class ConsumeTasksWorker implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ConsumeTasksWorker.class);

    private final int minThreads;
    private final int maxThreads;
    private final int pollingIntervalSeconds;
    private final BlockingQueue<Runnable> taskQueue;
    private final CompositeTaskConsumer compositeTaskConsumer;
    private final TaskScheduler taskScheduler;

    public ConsumeTasksWorker(
            int minThreads,
            int maxThreads,
            int pollingIntervalSeconds,
            CompositeTaskConsumer compositeTaskConsumer,
            TaskScheduler taskScheduler
    ) {
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
        this.pollingIntervalSeconds = pollingIntervalSeconds;
        this.taskQueue = new LinkedBlockingQueue<>(maxThreads);
        this.compositeTaskConsumer = compositeTaskConsumer;
        this.taskScheduler = taskScheduler;
    }

    @Override
    @Async(WorkersContextConfiguration.CONSUME_TASKS_WORKER_EXECUTOR_BEAN)
    public void run(String... args) {
        try {
            logger.info("Started processing tasks");
            internalRun();
        } catch (Exception e) {
            logger.error("Finished processing tasks with error", e);
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
            try {
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

                List<Task> tasks = taskScheduler.getTasksToConsume(freeThreads, scheduledAt);
                if (tasks.isEmpty()) {
                    logger.info("There are no tasks to process, waiting next iteration");
                    continue;
                }

                logger.info("Loaded {} tasks to process, adding them to executor", tasks.size());

                for (var task : tasks) {
                    executor.execute(() -> compositeTaskConsumer.processTask(task));
                }

                logger.info("Finished iteration");
            } catch (Exception e) {
                logger.error("Failed to process tasks", e);
            }
        }
    }
}
