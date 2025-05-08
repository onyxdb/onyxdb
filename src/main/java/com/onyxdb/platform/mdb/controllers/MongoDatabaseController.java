package com.onyxdb.platform.mdb.controllers;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ManagedMongoDbDatabasesApi;
import com.onyxdb.platform.generated.openapi.models.CreateMongoDatabaseRequestDTO;
import com.onyxdb.platform.generated.openapi.models.ListMongoDatabasesResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ScheduledOperationDTO;
import com.onyxdb.platform.idm.common.jwt.SecurityContextUtils;
import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.mdb.clusters.models.CreateDatabase;
import com.onyxdb.platform.mdb.clusters.models.Database;
import com.onyxdb.platform.mdb.databases.DatabaseMapper;
import com.onyxdb.platform.mdb.databases.DatabaseService;

@RestController
@RequiredArgsConstructor
public class MongoDatabaseController implements ManagedMongoDbDatabasesApi {
    private final DatabaseService databaseService;
    private final DatabaseMapper databaseMapper;

    @Override
    public ResponseEntity<ListMongoDatabasesResponseDTO> listDatabases(UUID clusterId) {
        List<Database> databases = databaseService.listDatabases(clusterId);
        var response = new ListMongoDatabasesResponseDTO(
                databases.stream().map(databaseMapper::mapToMongoDatabase).toList()
        );

        return ResponseEntity.ok()
                .body(response);
    }

    @Override
    public ResponseEntity<ScheduledOperationDTO> createDatabase(UUID clusterId, CreateMongoDatabaseRequestDTO rq) {
        Account account = SecurityContextUtils.getCurrentAccount();

        CreateDatabase createDatabase = databaseMapper.createMongoDatabaseRqToCreateDatabase(clusterId, rq, account.id());
        UUID operationId = databaseService.createDatabase(createDatabase);

        return ResponseEntity.ok(new ScheduledOperationDTO(operationId));
    }

    @Override
    public ResponseEntity<ScheduledOperationDTO> deleteDatabase(UUID clusterId, String databaseName) {
        Account account = SecurityContextUtils.getCurrentAccount();
        UUID operationId = databaseService.deleteDatabase(clusterId, databaseName, account.id());

        return ResponseEntity.ok(new ScheduledOperationDTO(operationId));
    }
}
