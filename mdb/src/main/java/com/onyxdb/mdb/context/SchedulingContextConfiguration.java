package com.onyxdb.mdb.context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author foxleren
 */
@Configuration
@EnableScheduling
public class SchedulingContextConfiguration  {
    public static final String PROCESS_CLUSTER_TASKS_EXECUTOR_NAME = "processClusterTasksExecutor";

//    @Bean(PROCESS_CLUSTER_TASKS_EXECUTOR_NAME)
//    public Executor processClusterTasksExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
////        executor.setCorePoolSize(5); // Set the initial number of threads in the pool
////        executor.setMaxPoolSize(10); // Set the maximum number of threads in the pool
////        executor.setQueueCapacity(25); // Set the queue capacity for holding pending tasks
//        executor.setThreadNamePrefix("AsyncTask1sss-"); // Set a prefix for thread names
//        executor.setThreadGroupName("AsyncTask2-");
//
//        return Executors.newSingleThreadExecutor(executor);
        // Set the RejectedExecutionHandler to log an error message
//        executor.setRejectedExecutionHandler((Runnable r, ThreadPoolExecutor e) -> {
//            // Log the error message indicating the task has been rejected due to the full queue
//            // You can customize this message based on your requirements
//            lo.error("Task Rejected: Thread pool is full. Increase the thread pool size.");
//        });

//        executor.initialize();
//        return executor;
//    }
//    public Executor processClusterTasksExecutor() {
//        var executor = new ThreadPoolTaskExecutor();
////        executor.setThreadNamePrefix("processClusterTasksExecutor-");
////        executor.setThreadGroupName("processClusterTasksExecutor-");
////        executor.initialize();
////        return executor;
//        return Executors.newSingleThreadExecutor(e -> {
//            return new Thread(e).start();
//        });
//    }
}
