package com.onyxdb.platform.mdb.controllers;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ManagedMongoDbDatabasesApi;
import com.onyxdb.platform.generated.openapi.models.CreateMongoDatabaseRequest;
import com.onyxdb.platform.generated.openapi.models.ListMongoDatabasesResponse;
import com.onyxdb.platform.generated.openapi.models.V1ScheduledOperationResponse;
import com.onyxdb.platform.mdb.clusters.models.CreateDatabase;
import com.onyxdb.platform.mdb.clusters.models.Database;
import com.onyxdb.platform.mdb.databases.DatabaseMapper;
import com.onyxdb.platform.mdb.databases.DatabaseService;

@RestController
@RequiredArgsConstructor
public class ManagedMongoDatabaseController implements ManagedMongoDbDatabasesApi {
    private final DatabaseService databaseService;
    private final DatabaseMapper databaseMapper;

    @Override
    public ResponseEntity<ListMongoDatabasesResponse> listDatabases(UUID clusterId) {
        List<Database> databases = databaseService.listDatabases(clusterId);
        var response = new ListMongoDatabasesResponse(
                databases.stream().map(databaseMapper::mapToMongoDatabase).toList()
        );

        return ResponseEntity.ok()
                .body(response);
    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> createDatabase(UUID clusterId, CreateMongoDatabaseRequest rq) {
        CreateDatabase createDatabase = databaseMapper.map(clusterId, rq);
        UUID operationId = databaseService.createDatabase(createDatabase);

        return ResponseEntity.ok()
                .body(new V1ScheduledOperationResponse(operationId));
    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> deleteDatabase(UUID clusterId, UUID databaseId) {
        UUID operationId = databaseService.deleteDatabase(clusterId, databaseId);

        return ResponseEntity.ok()
                .body(new V1ScheduledOperationResponse(operationId));
    }
}
