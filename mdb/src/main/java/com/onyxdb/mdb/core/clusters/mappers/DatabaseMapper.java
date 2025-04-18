package com.onyxdb.mdb.core.clusters.mappers;

import java.util.UUID;

import com.onyxdb.mdb.core.clusters.models.Database;
import com.onyxdb.mdb.core.clusters.models.DatabaseToCreate;
import com.onyxdb.mdb.generated.jooq.tables.records.DatabasesRecord;
import com.onyxdb.mdb.generated.openapi.models.CreateMongoDatabaseRequest;
import com.onyxdb.mdb.generated.openapi.models.MongoDatabase;
import com.onyxdb.mdb.utils.Consts;
import com.onyxdb.mdb.utils.TimeUtils;

public class DatabaseMapper {
    public DatabaseToCreate map(UUID clusterId, CreateMongoDatabaseRequest r) {
        return new DatabaseToCreate(
                r.getName(),
                clusterId,
                Consts.USER_ID
        );
    }

    public Database map(DatabaseToCreate d) {
        return new Database(
                UUID.randomUUID(),
                d.name(),
                d.clusterId(),
                TimeUtils.now(),
                d.createdBy(),
                false,
                null,
                null
        );
    }

    public DatabasesRecord map(Database d) {
        return new DatabasesRecord(
                d.id(),
                d.name(),
                d.clusterId(),
                d.createdAt(),
                d.createdBy(),
                d.isDeleted(),
                d.deletedAt(),
                d.deletedBy()
        );
    }

    public Database map(DatabasesRecord d) {
        return new Database(
                d.getId(),
                d.getName(),
                d.getClusterId(),
                d.getCreatedAt(),
                d.getCreatedBy(),
                d.getIsDeleted(),
                d.getDeletedAt(),
                d.getDeletedBy()
        );
    }

    public MongoDatabase mapToMongoDatabase(Database d) {
        return new MongoDatabase(
                d.id(),
                d.name(),
                d.clusterId(),
                d.createdAt(),
                d.createdBy(),
                d.isDeleted(),
                d.deletedAt(),
                d.deletedBy()
        );
    }
}
