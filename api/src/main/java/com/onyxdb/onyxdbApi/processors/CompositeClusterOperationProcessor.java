package com.onyxdb.onyxdbApi.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onyxdb.onyxdbApi.models.ClusterType;
import com.onyxdb.onyxdbApi.repositories.ClusterOperationRepository;

/**
 * @author foxleren
 */
public class CompositeClusterOperationProcessor {
    private final ClusterOperationRepository clusterOperationRepository;
    private final Map<ClusterType, ClusterOperationProcessor> clusterTypeToProcessor;

    public CompositeClusterOperationProcessor(
            ClusterOperationRepository clusterOperationRepository,
            List<ClusterOperationProcessor> processors)
    {
        this.clusterOperationRepository = clusterOperationRepository;
        this.clusterTypeToProcessor = new HashMap<>();
        prepareProcessorsMap(processors);
    }

//    public

    private void prepareProcessorsMap(List<ClusterOperationProcessor> processors) {
        for (ClusterOperationProcessor processor : processors) {
            clusterTypeToProcessor.put(processor.getClusterType(), processor);
        }
    }
}
