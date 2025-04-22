package com.onyxdb.platform.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.core.clusters.ClusterService;
import com.onyxdb.platform.core.clusters.mappers.DatabaseMapper;
import com.onyxdb.platform.core.clusters.models.Database;
import com.onyxdb.platform.core.clusters.models.DatabaseToCreate;
import com.onyxdb.platform.generated.openapi.apis.ManagedMongoDbDatabasesApi;
import com.onyxdb.platform.generated.openapi.models.CreateMongoDatabaseRequest;
import com.onyxdb.platform.generated.openapi.models.ListMongoDatabasesResponse;
import com.onyxdb.platform.generated.openapi.models.V1ScheduledOperationResponse;

@RestController
public class ManagedMongoDatabaseController implements ManagedMongoDbDatabasesApi {
    private final ClusterService clusterService;
    private final DatabaseMapper databaseMapper;

    public ManagedMongoDatabaseController(ClusterService clusterService, DatabaseMapper databaseMapper) {
        this.clusterService = clusterService;
        this.databaseMapper = databaseMapper;
    }

    @Override
    public ResponseEntity<ListMongoDatabasesResponse> listDatabases(UUID clusterId) {
        List<Database> databases = clusterService.listDatabases(clusterId);
        var response = new ListMongoDatabasesResponse(
                databases.stream().map(databaseMapper::mapToMongoDatabase).toList()
        );

        return ResponseEntity.ok()
                .body(response);
    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> createDatabase(UUID clusterId, CreateMongoDatabaseRequest rq) {
        DatabaseToCreate databaseToCreate = databaseMapper.map(clusterId, rq);
        UUID operationId = clusterService.createDatabase(databaseToCreate);

        return ResponseEntity.ok()
                .body(new V1ScheduledOperationResponse(operationId));
    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> deleteDatabase(UUID clusterId, UUID databaseId) {
        UUID operationId = clusterService.deleteDatabase(clusterId, databaseId);

        return ResponseEntity.ok()
                .body(new V1ScheduledOperationResponse(operationId));
    }
}
