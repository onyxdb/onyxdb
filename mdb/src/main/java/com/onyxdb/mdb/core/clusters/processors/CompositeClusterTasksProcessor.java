package com.onyxdb.mdb.core.clusters.processors;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.models.ClusterOperationStatus;
import com.onyxdb.mdb.core.clusters.models.ClusterTask;
import com.onyxdb.mdb.core.clusters.models.ClusterTaskStatus;
import com.onyxdb.mdb.core.clusters.models.ClusterType;
import com.onyxdb.mdb.exceptions.ClusterTaskHasNoAttemptsException;
import com.onyxdb.mdb.exceptions.InternalServerErrorException;
import com.onyxdb.mdb.services.BaseClusterService;

/**
 * @author foxleren
 */
public class CompositeClusterTasksProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CompositeClusterTasksProcessor.class);

    private static final Duration DEFAULT_SCHEDULED_AT_DELAY = Duration.ofSeconds(10);

    private final Map<ClusterType, ClusterTaskProcessor> clusterTypeToProcessor;
    private final BaseClusterService clusterServiceOld;
    private final ClusterService clusterServiceNew;

    public CompositeClusterTasksProcessor(
            List<ClusterTaskProcessor> processors,
            BaseClusterService clusterServiceOld,
            ClusterService clusterServiceNew
    ) {
        this.clusterTypeToProcessor = buildClusterTypeToProcessor(processors);
        this.clusterServiceOld = clusterServiceOld;
        this.clusterServiceNew = clusterServiceNew;
    }

    public void processTask(ClusterTask task) {
        ClusterType clusterType = task.clusterType();

        if (!clusterTypeToProcessor.containsKey(clusterType)) {
            throw new InternalServerErrorException(String.format(
                    "Can't find processor for clusterType=%s", clusterType
            ));
        }

        ClusterTaskProcessor processor = clusterTypeToProcessor.get(clusterType);

        if (task.isFirst()) {
            clusterServiceOld.updateTaskAndOperationStatus(
                    task.id(),
                    ClusterTaskStatus.IN_PROGRESS,
                    null,
                    task.operationId(),
                    ClusterOperationStatus.IN_PROGRESS
            );
        }

        try {
            if (task.hasNoAttempts()) {
                throw new ClusterTaskHasNoAttemptsException(task.id());
            }

            ClusterTaskProcessingResult processingResult = processor.process(task);
            handleProcessingResult(task, processingResult);
        } catch (Exception e) {
            logger.error("Failed to process cluster task with id={}", task.id(), e);

            if (task.attemptsLeft() > 1) {
                clusterServiceNew.updateTask(
                        task.id(),
                        ClusterTaskStatus.SCHEDULED,
                        task.attemptsLeft() - 1,
                        task.getScheduledAtWithDelay(DEFAULT_SCHEDULED_AT_DELAY)
                );
            } else {
                clusterServiceOld.updateTaskAndOperationStatus(
                        task.id(),
                        ClusterTaskStatus.ERROR,
                        0,
                        task.operationId(),
                        ClusterOperationStatus.ERROR
                );
            }
        }
    }

    private void handleProcessingResult(ClusterTask task, ClusterTaskProcessingResult processingResult) {
        if (processingResult.status().equalsStringEnum(ClusterTaskStatus.SCHEDULED)) {
            logger.info("Rescheduling task with id={}", task.id());

            if (task.hasNoAttempts()) {
                throw new ClusterTaskHasNoAttemptsException(task.id());
            }

            clusterServiceNew.updateTask(
                    task.id(),
                    ClusterTaskStatus.SCHEDULED,
                    task.attemptsLeft() - 1,
                    processingResult.scheduledAt()
            );
            return;
        }

        if (task.isLast()) {
            clusterServiceOld.updateTaskAndOperationStatus(
                    task.id(),
                    ClusterTaskStatus.SUCCESS,
                    null,
                    task.operationId(),
                    ClusterOperationStatus.SUCCESS
            );
            return;
        }
        clusterServiceOld.updateTaskStatus(task.id(), ClusterTaskStatus.SUCCESS);
    }

    private static Map<ClusterType, ClusterTaskProcessor> buildClusterTypeToProcessor(
            List<ClusterTaskProcessor> processors
    ) {
        Map<ClusterType, ClusterTaskProcessor> map = new HashMap<>();
        for (ClusterTaskProcessor processor : processors) {
            map.put(processor.getClusterType(), processor);
        }
        return map;
    }
}
