package com.onyxdb.platform.mdb.backups;

import java.time.LocalDateTime;

import org.jetbrains.annotations.Nullable;

public record Backup(
        String name,
        BackupType type,
        BackupStatus status,
        @Nullable
        LocalDateTime startedAt,
        @Nullable
        LocalDateTime finishedAt
) {
}
