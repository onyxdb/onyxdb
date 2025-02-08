package com.onyxdb.mdb.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.mdb.generated.jooq.tables.records.ProjectsRecord;

/**
 * @author foxleren
 */
public record Project(
        UUID id,
        String name,
        LocalDateTime createdAt,
        UUID createdBy,
        UUID ownerId
) {
    public ProjectsRecord toJooqProjectRecord() {
        return new ProjectsRecord(
                id,
                name,
                createdAt,
                createdBy,
                ownerId
        );
    }

    public static Project fromProjectToCreate(ProjectToCreate p) {
        return new Project(
                UUID.randomUUID(),
                p.name(),
                LocalDateTime.now(),
                p.ownerId(),
                p.ownerId()
        );
    }
}
