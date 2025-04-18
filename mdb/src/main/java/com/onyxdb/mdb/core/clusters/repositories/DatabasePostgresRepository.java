package com.onyxdb.mdb.core.clusters.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;

import com.onyxdb.mdb.core.clusters.mappers.DatabaseMapper;
import com.onyxdb.mdb.core.clusters.models.Database;
import com.onyxdb.mdb.core.projects.Project;
import com.onyxdb.mdb.core.projects.ProjectConverter;
import com.onyxdb.mdb.exceptions.BadRequestException;
import com.onyxdb.mdb.generated.jooq.Keys;
import com.onyxdb.mdb.generated.jooq.tables.records.DatabasesRecord;
import com.onyxdb.mdb.utils.PsqlUtils;
import com.onyxdb.mdb.utils.TimeUtils;

import static com.onyxdb.mdb.generated.jooq.Tables.DATABASES;
import static com.onyxdb.mdb.generated.jooq.Tables.PROJECTS;

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
                            "Database with name '%s' exists or was deleted earlier", database.name()
                    ))
            );

            throw e;
        }
    }

    @Override
    public void markDatabaseAsDeleted(UUID databaseId, UUID deletedBy) {
        dslContext.update(DATABASES)
                .set(DATABASES.IS_DELETED, true)
                .set(DATABASES.DELETED_AT, TimeUtils.now())
                .set(DATABASES.DELETED_BY, deletedBy)
                .where(DATABASES.ID.eq(databaseId))
                .execute();
    }
}
