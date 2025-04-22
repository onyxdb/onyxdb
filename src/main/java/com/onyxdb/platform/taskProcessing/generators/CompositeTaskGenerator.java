package com.onyxdb.platform.taskProcessing.generators;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.onyxdb.platform.exceptions.InternalServerErrorException;
import com.onyxdb.platform.taskProcessing.models.OperationType;
import com.onyxdb.platform.taskProcessing.models.TaskWithBlockers;
import com.onyxdb.platform.taskProcessing.models.payloads.ClusterTaskPayload;

/**
 * @author foxleren
 */
public class CompositeTaskGenerator {
    private final Map<OperationType, ClusterTaskGenerator> operationTypeToClusterTaskGenerator;

    public CompositeTaskGenerator(Map<OperationType, ClusterTaskGenerator> operationTypeToClusterTaskGenerator) {
        this.operationTypeToClusterTaskGenerator = operationTypeToClusterTaskGenerator;
    }

    public List<TaskWithBlockers> generateClusterTasks(
            UUID operationId,
            OperationType operationType,
            UUID clusterId
    ) {
        if (!operationTypeToClusterTaskGenerator.containsKey(operationType)) {
            throw new InternalServerErrorException(String.format(
                    "Can't find tasks generator for operation type '%s'", operationType
            ));
        }

        ClusterTaskGenerator generator = operationTypeToClusterTaskGenerator.get(operationType);
        ClusterTaskPayload payload = new ClusterTaskPayload(clusterId);
        return generator.generateTasks(operationId, payload);
    }
}
