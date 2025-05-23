package com.onyxdb.platform.mdb.databases;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.clusters.ClusterRepository;
import com.onyxdb.platform.mdb.clusters.models.CreateDatabase;
import com.onyxdb.platform.mdb.clusters.models.Database;
import com.onyxdb.platform.mdb.operations.models.Operation;
import com.onyxdb.platform.mdb.operations.models.OperationType;
import com.onyxdb.platform.mdb.operations.models.payload.MongoCreateDatabasePayload;
import com.onyxdb.platform.mdb.operations.models.payload.MongoDeleteDatabasePayload;
import com.onyxdb.platform.mdb.operations.repositories.OperationRepository;
import com.onyxdb.platform.mdb.utils.ObjectMapperUtils;

@Service
@RequiredArgsConstructor
public class DatabaseService {
    private final DatabaseRepository databaseRepository;
    private final DatabaseMapper databaseMapper;
    private final OperationRepository operationRepository;
    private final TransactionTemplate transactionTemplate;
    private final ObjectMapper objectMapper;
    private final ClusterRepository clusterRepository;

    public List<Database> listDatabases(UUID clusterId) {
        return databaseRepository.listDatabases(clusterId);
    }

    public UUID createDatabase(CreateDatabase createDatabase) {
        clusterRepository.getClusterOrThrow(createDatabase.clusterId());

        Database database = databaseMapper.databaseToCreateToDatabase(createDatabase);
        var operation = Operation.scheduledWithPayload(
                OperationType.MONGO_CREATE_DATABASE,
                database.clusterId(),
                createDatabase.createdBy(),
                ObjectMapperUtils.convertToString(objectMapper, new MongoCreateDatabasePayload(
                        database.clusterId(),
                        database.name()
                ))
        );

        transactionTemplate.executeWithoutResult(status -> {
            databaseRepository.createDatabase(database);
            operationRepository.createOperation(operation);
        });

        return operation.id();
    }

    public UUID deleteDatabase(UUID clusterId, String databaseName, UUID deletedBy) {
        Database database = databaseRepository.getDatabase(clusterId, databaseName);

        var operation = Operation.scheduledWithPayload(
                OperationType.MONGO_DELETE_DATABASE,
                clusterId,
                deletedBy,
                ObjectMapperUtils.convertToString(objectMapper, new MongoDeleteDatabasePayload(
                        clusterId,
                        database.name(),
                        deletedBy
                ))
        );
        operationRepository.createOperation(operation);

        return operation.id();
    }
}
