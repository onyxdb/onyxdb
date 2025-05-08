package com.onyxdb.platform.mdb.operations.producers;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.exceptions.InternalServerErrorException;
import com.onyxdb.platform.mdb.operations.models.OperationType;
import com.onyxdb.platform.mdb.operations.producers.mongo.MongoCreateBackupTaskProducer;
import com.onyxdb.platform.mdb.operations.producers.mongo.MongoCreateClusterTaskProducer;
import com.onyxdb.platform.mdb.operations.producers.mongo.MongoCreateDatabaseTaskProducer;
import com.onyxdb.platform.mdb.operations.producers.mongo.MongoCreateUserTaskProducer;
import com.onyxdb.platform.mdb.operations.producers.mongo.MongoDeleteClusterTaskProducer;
import com.onyxdb.platform.mdb.operations.producers.mongo.MongoDeleteDatabaseTaskProducer;
import com.onyxdb.platform.mdb.operations.producers.mongo.MongoDeleteUserTaskProducer;
import com.onyxdb.platform.mdb.operations.producers.mongo.MongoModifyClusterTaskProducer;

@Component
public class TaskProducerProvider {
    private final Map<OperationType, TaskProducer<?>> operationTypeToTaskProducer;

    public TaskProducerProvider(
            MongoCreateClusterTaskProducer mongoCreateClusterTaskProducer,
            MongoDeleteClusterTaskProducer mongoDeleteClusterTaskProducer,
            MongoModifyClusterTaskProducer mongoModifyClusterTaskProducer,
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
                        mongoDeleteClusterTaskProducer
                ),
                Map.entry(
                        OperationType.MONGO_MODIFY_CLUSTER,
                        mongoModifyClusterTaskProducer
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
