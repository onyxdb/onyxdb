package com.onyxdb.platform.mdb.operationsOLD.tasks;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class TaskWorker<OPERATION_PAYLOAD> {
    private static final Logger logger = LoggerFactory.getLogger(TaskWorker.class);
    @Scheduled(fixedRateString = "PT2S")
    @SchedulerLock(
            name = "TaskWorker",
            lockAtLeastFor = "1s",
            lockAtMostFor = "1s"
    )
    public void execute() {
        logger.info("PERFORMING!!!");
    }
}
