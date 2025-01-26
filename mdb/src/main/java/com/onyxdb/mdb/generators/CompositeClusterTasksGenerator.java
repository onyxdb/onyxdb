package com.onyxdb.mdb.generators;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.onyxdb.mdb.exceptions.InternalServerErrorException;
import com.onyxdb.mdb.models.ClusterOperationType;
import com.onyxdb.mdb.models.ClusterTask;
import com.onyxdb.mdb.models.ClusterType;

/**
 * @author foxleren
 */
public class CompositeClusterTasksGenerator {
    private final Map<ClusterType, ClusterTasksGenerator> clusterTypeToGenerator;

    public CompositeClusterTasksGenerator(List<ClusterTasksGenerator> generators) {
        this.clusterTypeToGenerator = prepareGeneratorsMap(generators);
    }

    public List<ClusterTask> generateTasks(
            UUID clusterId,
            ClusterType clusterType,
            UUID operationId,
            ClusterOperationType clusterOperationType)
    {
        if (!clusterTypeToGenerator.containsKey(clusterType)) {
            throw new InternalServerErrorException(String.format(
                    "Can't find tasks generator for cluster type '%s'", clusterType
            ));
        }

        ClusterTasksGenerator generator = clusterTypeToGenerator.get(clusterType);
        return generator.generateTasks(clusterId, operationId, clusterOperationType);
    }

    private static Map<ClusterType, ClusterTasksGenerator> prepareGeneratorsMap(
            List<ClusterTasksGenerator> generators)
    {
        return generators
                .stream()
                .collect(Collectors.toMap(ClusterTasksGenerator::getClusterType, Function.identity()));
    }
}
