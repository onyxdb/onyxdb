package com.onyxdb.platform.mdb.backups;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.onyxdb.platform.generated.openapi.models.BackupStatusDTO;
import com.onyxdb.platform.generated.openapi.models.BackupTypeDTO;
import com.onyxdb.platform.generated.openapi.models.MongoBackupDTO;

@Component
public class BackupMapper {
    public MongoBackupDTO backupToMongoBackupDTO(Backup backup, UUID clusterId) {
        return new MongoBackupDTO(
                backup.name(),
                clusterId,
                new BackupTypeDTO(
                        backup.type().value(),
                        backup.type().getDisplayValue()
                ),
                new BackupStatusDTO(
                        backup.status().value(),
                        backup.status().getDisplayValue()
                ),
                backup.status().equalsStringEnum(BackupStatus.READY),
                backup.startedAt(),
                backup.finishedAt()
        );
    }
}
