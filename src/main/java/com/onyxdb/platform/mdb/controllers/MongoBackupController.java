package com.onyxdb.platform.mdb.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ManagedMongoDbBackupsApi;
import com.onyxdb.platform.generated.openapi.models.ListMongoBackupsResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ScheduledOperationDTO;
import com.onyxdb.platform.idm.common.jwt.SecurityContextUtils;
import com.onyxdb.platform.idm.models.Account;
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
        Account account = SecurityContextUtils.getCurrentAccount();
        UUID operationId = backupService.createBackup(clusterId, account.id());

        return ResponseEntity.ok(new ScheduledOperationDTO(operationId));
    }

    @Override
    public ResponseEntity<ScheduledOperationDTO> deleteBackup(UUID clusterId, String backupName) {
        Account account = SecurityContextUtils.getCurrentAccount();
        UUID operationId = backupService.deleteBackup(clusterId, backupName, account.id());

        return ResponseEntity.ok(new ScheduledOperationDTO(operationId));
    }
}
