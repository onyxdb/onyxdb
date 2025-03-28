package com.onyxdb.mdb.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onyxdb.mdb.core.clusters.models.ClusterType;
import com.onyxdb.mdb.exceptions.InternalServerErrorException;
import com.onyxdb.mdb.models.ClusterOperationStatus;
import com.onyxdb.mdb.models.ClusterTask;
import com.onyxdb.mdb.models.ClusterTaskStatus;
import com.onyxdb.mdb.services.BaseClusterService;

/**
 * @author foxleren
 */
public class CompositeClusterTasksProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CompositeClusterTasksProcessor.class);

    private final Map<ClusterType, ClusterTaskProcessor> clusterTypeToProcessor;
    private final BaseClusterService clusterService;

    public CompositeClusterTasksProcessor(
            List<ClusterTaskProcessor> processors,
            BaseClusterService clusterService
    ) {
        this.clusterTypeToProcessor = buildClusterTypeToProcessor(processors);
        this.clusterService = clusterService;
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
            clusterService.updateTaskAndOperationStatus(
                    task.id(),
                    ClusterTaskStatus.IN_PROGRESS,
                    task.operationId(),
                    ClusterOperationStatus.IN_PROGRESS
            );
        }

        try {
            processor.process(task);
        } catch (Exception e) {
            logger.error("Failed to process cluster task with id={}", task, e);
            clusterService.updateTaskAndOperationStatus(
                    task.id(),
                    ClusterTaskStatus.ERROR,
                    task.operationId(),
                    ClusterOperationStatus.ERROR
            );
            return;
        }

        if (task.isLast()) {
            clusterService.updateTaskAndOperationStatus(
                    task.id(),
                    ClusterTaskStatus.SUCCESS,
                    task.operationId(),
                    ClusterOperationStatus.SUCCESS
            );
            return;
        }
        clusterService.updateTaskStatus(task.id(), ClusterTaskStatus.SUCCESS);
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
