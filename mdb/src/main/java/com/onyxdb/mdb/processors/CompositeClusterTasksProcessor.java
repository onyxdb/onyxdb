package com.onyxdb.mdb.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onyxdb.mdb.exceptions.InternalServerErrorException;
import com.onyxdb.mdb.models.ClusterOperation;
import com.onyxdb.mdb.models.ClusterTask;
import com.onyxdb.mdb.models.ClusterType;

/**
 * @author foxleren
 */
public class CompositeClusterTasksProcessor {
    private final Map<ClusterType, ClusterOperationProcessor> clusterTypeToProcessor;

    public CompositeClusterTasksProcessor(List<ClusterOperationProcessor> processors) {
        this.clusterTypeToProcessor = buildClusterTypeToProcessor(processors);
    }

    public void processTask(ClusterTask task) {
//        ClusterType clusterType = operation.clusterType();
//
//        if (!clusterTypeToProcessor.containsKey(clusterType)) {
//            throw new InternalServerErrorException(String.format(
//                    "Can't find processor for clusterType=%s", clusterType
//            ));
//        }
//
//        ClusterOperationProcessor processor = clusterTypeToProcessor.get(clusterType);
//        processor.process(operation);
    }

    private static Map<ClusterType, ClusterOperationProcessor> buildClusterTypeToProcessor(
            List<ClusterOperationProcessor> processors)
    {
        Map<ClusterType, ClusterOperationProcessor> map = new HashMap<>();
        for (ClusterOperationProcessor processor : processors) {
            map.put(processor.getClusterType(), processor);
        }
        return map;
    }
}
