package com.onyxdb.platform.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ManagedMongoDbBackupsApi;
import com.onyxdb.platform.generated.openapi.models.BackupType;
import com.onyxdb.platform.generated.openapi.models.ListMongoBackupsResponse;
import com.onyxdb.platform.generated.openapi.models.MongoBackup;
import com.onyxdb.platform.generated.openapi.models.V1ScheduledOperationResponse;
import com.onyxdb.platform.processing.models.Operation;
import com.onyxdb.platform.processing.models.OperationType;
import com.onyxdb.platform.processing.models.payloads.ClusterPayload;
import com.onyxdb.platform.processing.repositories.OperationRepository;
import com.onyxdb.platform.utils.ObjectMapperUtils;
import com.onyxdb.platform.utils.TimeUtils;

@RestController
public class ManagedMongoBackupController implements ManagedMongoDbBackupsApi {
    private final OperationRepository operationRepository;
    private final ObjectMapper objectMapper;
    private boolean isStarted = false;
    private LocalDateTime lastBackupTime;

    public ManagedMongoBackupController(OperationRepository operationRepository, ObjectMapper objectMapper) {
        this.operationRepository = operationRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<ListMongoBackupsResponse> listBackups(UUID clusterId) {
        if (!isStarted) {
            return ResponseEntity.ok(new ListMongoBackupsResponse(List.of()));
        }

        return ResponseEntity.ok(new ListMongoBackupsResponse(
                List.of(
//                        new MongoBackup(
//                                "cron-test-backup7-san-20250419160900-fcdgh",
//                                UUID.randomUUID(),
//                                BackupType.AUTOMATED,
//                                TimeUtils.now(),
//                                TimeUtils.now()
//                        ),
                        new MongoBackup(
                                "cron-backup-20250419160800-cjllk",
                                UUID.randomUUID(),
                                BackupType.MANUAL,
                                lastBackupTime,
                                null
//                                TimeUtils.now()
                        )
                )
        ));
    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> createBackup(UUID clusterId) {
        var operation = Operation.scheduledWithPayload(
                OperationType.MONGO_CREATE_BACKUP,
                clusterId,
                ObjectMapperUtils.convertToString(objectMapper, new ClusterPayload(
                        clusterId
                ))
        );
        operationRepository.createOperation(operation);
        isStarted = true;
        lastBackupTime = TimeUtils.now();
        return ResponseEntity.ok(new V1ScheduledOperationResponse(UUID.randomUUID()));
    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> deleteBackup(UUID clusterId, String backupName) {
        return ResponseEntity.ok(new V1ScheduledOperationResponse(UUID.randomUUID()));
    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> restoreFromBackup(UUID clusterId, String backupName) {
        return ResponseEntity.ok(new V1ScheduledOperationResponse(UUID.randomUUID()));
    }
}
