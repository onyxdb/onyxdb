package com.onyxdb.platform.mdb.databases;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;

import com.onyxdb.platform.core.clusters.models.Database;
import com.onyxdb.platform.exceptions.BadRequestException;
import com.onyxdb.platform.generated.jooq.Keys;
import com.onyxdb.platform.generated.jooq.tables.records.DatabasesRecord;
import com.onyxdb.platform.utils.PsqlUtils;
import com.onyxdb.platform.utils.TimeUtils;

import static com.onyxdb.platform.generated.jooq.Tables.DATABASES;

public class DatabasePostgresRepository implements DatabaseRepository {
    private final DSLContext dslContext;
    private final DatabaseMapper databaseMapper;

    public DatabasePostgresRepository(DSLContext dslContext, DatabaseMapper databaseMapper) {
        this.dslContext = dslContext;
        this.databaseMapper = databaseMapper;
    }

    @Override
    public List<Database> listDatabases(UUID clusterId) {
        return dslContext.select()
                .from(DATABASES)
                .where(DATABASES.CLUSTER_ID.eq(clusterId)
                        .and(DATABASES.IS_DELETED.eq(false))
                )
                .fetch(r -> databaseMapper.map(r.into(DatabasesRecord.class)));
    }

    @Override
    public Optional<Database> getDatabaseO(UUID clusterId, UUID databaseId) {
        return dslContext.select()
                .from(DATABASES)
                .where(DATABASES.CLUSTER_ID.eq(clusterId)
                        .and(DATABASES.ID.eq(databaseId))
                        .and(DATABASES.IS_DELETED.eq(false))
                )
                .fetchOptional()
                .map(r -> databaseMapper.map(r.into(DatabasesRecord.class)));
    }

    @Override
    public Database getDatabase(UUID clusterId, UUID databaseId) {
        return getDatabaseO(clusterId, databaseId).orElseThrow(()-> new RuntimeException("Database not found"));
    }

    @Override
    public void createDatabase(Database database) {
        try {
            dslContext.insertInto(DATABASES)
                    .set(databaseMapper.map(database))
                    .execute();
        } catch (DataAccessException e) {
            PsqlUtils.handleDataAccessEx(
                    e,
                    DATABASES,
                    Keys.DATABASES_NAME_CLUSTER_ID_KEY,
                    () -> new BadRequestException(String.format(
                            "Database with databaseName '%s' exists or was deleted earlier", database.name()
                    ))
            );

            throw e;
        }
    }

    @Override
    public void markDatabaseAsDeleted(UUID databaseId, UUID deletedBy) {
        System.out.println("Marking " + databaseId + " as deleted by " + deletedBy);
        dslContext.update(DATABASES)
                .set(DATABASES.IS_DELETED, true)
                .set(DATABASES.DELETED_AT, TimeUtils.now())
                .set(DATABASES.DELETED_BY, deletedBy)
                .where(DATABASES.ID.eq(databaseId))
                .execute();
    }
}
