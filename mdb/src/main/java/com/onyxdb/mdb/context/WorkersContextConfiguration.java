package com.onyxdb.mdb.context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.workers.ProcessClusterTasksWorker;

/**
 * @author foxleren
 */
@Configuration
public class WorkersContextConfiguration {
    @Bean
    public CommandLineRunner startProcessClusterTasksWorker(ProcessClusterTasksWorker processClusterTasksWorker) {
        return args -> {
            try (ExecutorService executorService = Executors.newSingleThreadExecutor()) {
                executorService.execute(processClusterTasksWorker);
            }
        };
    }
}
