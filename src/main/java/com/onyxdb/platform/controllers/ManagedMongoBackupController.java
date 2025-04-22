package com.onyxdb.platform.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ManagedMongoDbBackupsApi;
import com.onyxdb.platform.generated.openapi.models.BackupType;
import com.onyxdb.platform.generated.openapi.models.ListMongoBackupsResponse;
import com.onyxdb.platform.generated.openapi.models.MongoBackup;
import com.onyxdb.platform.generated.openapi.models.V1ScheduledOperationResponse;
import com.onyxdb.platform.utils.TimeUtils;

@RestController
public class ManagedMongoBackupController implements ManagedMongoDbBackupsApi {
    @Override
    public ResponseEntity<ListMongoBackupsResponse> listBackups(UUID clusterId) {
        return ResponseEntity.ok(new ListMongoBackupsResponse(
                List.of(
                        new MongoBackup(
                                "cron-test-backup7-san-20250419160900-fcdgh",
                                UUID.randomUUID(),
                                BackupType.AUTOMATED,
                                TimeUtils.now(),
                                TimeUtils.now()
                        ),
                        new MongoBackup(
                                "cron-test-backup7-san-20250419160800-cjllk",
                                UUID.randomUUID(),
                                BackupType.AUTOMATED,
                                TimeUtils.now(),
                                TimeUtils.now()
                        )
                )
        ));
    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> createBackup(UUID clusterId) {
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
