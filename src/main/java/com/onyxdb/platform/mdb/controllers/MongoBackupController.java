package com.onyxdb.platform.mdb.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ManagedMongoDbBackupsApi;
import com.onyxdb.platform.generated.openapi.models.ListMongoBackupsResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ScheduledOperationDTO;
import com.onyxdb.platform.mdb.backups.Backup;
import com.onyxdb.platform.mdb.backups.BackupMapper;
import com.onyxdb.platform.mdb.backups.BackupService;

@RestController
public class MongoBackupController implements ManagedMongoDbBackupsApi {
    private final BackupService backupService;
    private final BackupMapper backupMapper;

    public MongoBackupController(BackupService backupService, BackupMapper backupMapper) {
        this.backupService = backupService;
        this.backupMapper = backupMapper;
    }

    @Override
    public ResponseEntity<ListMongoBackupsResponseDTO> listBackups(UUID clusterId) {
        List<Backup> backups = backupService.listBackups(clusterId);
        var response = new ListMongoBackupsResponseDTO(
                backups.stream().map(backup -> backupMapper.backupToMongoBackupDTO(backup, clusterId)).toList()
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ScheduledOperationDTO> createBackup(UUID clusterId) {
        UUID operationId = backupService.createBackup(clusterId);
        return ResponseEntity.ok(new ScheduledOperationDTO(operationId));
    }

    @Override
    public ResponseEntity<ScheduledOperationDTO> deleteBackup(UUID clusterId, String backupName) {
        return null;
    }

//    @Override
//    public ResponseEntity<V1ScheduledOperationResponse> deleteBackup(UUID clusterId, String backupName) {
//        return ResponseEntity.ok(new V1ScheduledOperationResponse(UUID.randomUUID()));
//    }
//
//    @Override
//    public ResponseEntity<V1ScheduledOperationResponse> restoreFromBackup(UUID clusterId, String backupName) {
//        return ResponseEntity.ok(new V1ScheduledOperationResponse(UUID.randomUUID()));
//    }
}
