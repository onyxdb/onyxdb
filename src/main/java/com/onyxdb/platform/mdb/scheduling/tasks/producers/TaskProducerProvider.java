package com.onyxdb.platform.mdb.scheduling.tasks.producers;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.exceptions.InternalServerErrorException;
import com.onyxdb.platform.mdb.scheduling.operations.models.OperationType;
import com.onyxdb.platform.mdb.scheduling.tasks.producers.mongo.MongoCreateBackupTaskProducer;
import com.onyxdb.platform.mdb.scheduling.tasks.producers.mongo.MongoCreateClusterTaskProducer;
import com.onyxdb.platform.mdb.scheduling.tasks.producers.mongo.MongoCreateDatabaseTaskProducer;
import com.onyxdb.platform.mdb.scheduling.tasks.producers.mongo.MongoCreateUserTaskProducer;
import com.onyxdb.platform.mdb.scheduling.tasks.producers.mongo.MongoDeleteClusterTaskGenerator;
import com.onyxdb.platform.mdb.scheduling.tasks.producers.mongo.MongoDeleteDatabaseTaskProducer;
import com.onyxdb.platform.mdb.scheduling.tasks.producers.mongo.MongoDeleteUserTaskProducer;
import com.onyxdb.platform.mdb.scheduling.tasks.producers.mongo.MongoScaleClusterTaskProducer;

@Component
public class TaskProducerProvider {
    private final Map<OperationType, TaskProducer<?>> operationTypeToTaskProducer;

    public TaskProducerProvider(
            MongoCreateClusterTaskProducer mongoCreateClusterTaskProducer,
            MongoDeleteClusterTaskGenerator mongoDeleteClusterTaskGenerator,
            MongoScaleClusterTaskProducer mongoScaleClusterTaskProducer,
            MongoCreateDatabaseTaskProducer mongoCreateDatabaseTaskProducer,
            MongoDeleteDatabaseTaskProducer mongoDeleteDatabaseTaskProducer,
            MongoCreateUserTaskProducer mongoCreateUserTaskProducer,
            MongoDeleteUserTaskProducer mongoDeleteUserTaskProducer,
            MongoCreateBackupTaskProducer mongoCreateBackupTaskProducer
    ) {
        this.operationTypeToTaskProducer = Map.ofEntries(
                Map.entry(
                        OperationType.MONGO_CREATE_CLUSTER,
                        mongoCreateClusterTaskProducer
                ),
                Map.entry(
                        OperationType.MONGO_DELETE_CLUSTER,
                        mongoDeleteClusterTaskGenerator
                ),
                Map.entry(
                        OperationType.MONGO_SCALE_CLUSTER,
                        mongoScaleClusterTaskProducer
                ),
                Map.entry(
                        OperationType.MONGO_CREATE_DATABASE,
                        mongoCreateDatabaseTaskProducer
                ),
                Map.entry(
                        OperationType.MONGO_DELETE_DATABASE,
                        mongoDeleteDatabaseTaskProducer
                ),
                Map.entry(
                        OperationType.MONGO_CREATE_USER,
                        mongoCreateUserTaskProducer
                ),
                Map.entry(
                        OperationType.MONGO_DELETE_USER,
                        mongoDeleteUserTaskProducer
                ),
                Map.entry(
                        OperationType.MONGO_CREATE_BACKUP,
                        mongoCreateBackupTaskProducer
                )
        );
    }

    public TaskProducer<?> getTaskProducerOrThrow(OperationType operationType) {
        if (operationTypeToTaskProducer.containsKey(operationType)) {
            return operationTypeToTaskProducer.get(operationType);
        }

        throw new InternalServerErrorException(String.format(
                "Can't find task producer for operation type '%s'", operationType
        ));
    }
}
