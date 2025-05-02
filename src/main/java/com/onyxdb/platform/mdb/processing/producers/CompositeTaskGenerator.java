package com.onyxdb.platform.mdb.processing.producers;

import org.springframework.stereotype.Service;

/**
 * @author foxleren
 */
@Service
public class CompositeTaskGenerator {
//    private final Map<OperationType, ClusterTaskGenerator> operationTypeToClusterTaskGenerator;

    public CompositeTaskGenerator() {
//        this.operationTypeToClusterTaskGenerator = operationTypeToClusterTaskGenerator;
    }

//    public List<TaskWithBlockers> generateClusterTasks(
//            UUID operationId,
//            OperationType operationType,
//            UUID id
//    ) {
//        if (!operationTypeToClusterTaskGenerator.containsKey(operationType)) {
//            throw new InternalServerErrorException(String.format(
//                    "Can't find tasks generator for operation type '%s'", operationType
//            ));
//        }
//
//        ClusterTaskGenerator generator = operationTypeToClusterTaskGenerator.get(operationType);
//        ClusterTaskPayload payload = new ClusterTaskPayload(id);
//        return generator.generateTasks(operationId, payload);
//    }
}
