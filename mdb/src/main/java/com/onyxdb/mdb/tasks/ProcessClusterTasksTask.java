package com.onyxdb.mdb.tasks;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author foxleren
 */
@Component
//@EnableAsync
public class ProcessClusterTasksTask {
    @Scheduled(
            initialDelayString = "${onyxdb-app.tasks.process-cluster-operations.initial-delay-string}",
            fixedRateString = "${onyxdb-app.tasks.process-cluster-operations.fixed-rate-string}"
    )
//    @Async
    public void execute() throws InterruptedException {
        System.out.println("Start");
        Thread.sleep(100000);
        System.out.println("Finish");
    }
}
