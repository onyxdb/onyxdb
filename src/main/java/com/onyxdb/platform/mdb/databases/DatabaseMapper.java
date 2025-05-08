package com.onyxdb.platform.mdb.databases;

import java.util.UUID;

import com.onyxdb.platform.generated.jooq.tables.records.DatabasesRecord;
import com.onyxdb.platform.generated.openapi.models.CreateMongoDatabaseRequestDTO;
import com.onyxdb.platform.generated.openapi.models.MongoDatabaseDTO;
import com.onyxdb.platform.mdb.clusters.models.CreateDatabase;
import com.onyxdb.platform.mdb.clusters.models.Database;
import com.onyxdb.platform.mdb.utils.TimeUtils;

public class DatabaseMapper {
    public CreateDatabase createMongoDatabaseRqToCreateDatabase(
            UUID clusterId,
            CreateMongoDatabaseRequestDTO rq,
            UUID createdBy
    ) {
        return new CreateDatabase(
                rq.getName(),
                clusterId,
                createdBy
        );
    }

    public Database databaseToCreateToDatabase(CreateDatabase d) {
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

    public MongoDatabaseDTO mapToMongoDatabase(Database d) {
        return new MongoDatabaseDTO(
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
