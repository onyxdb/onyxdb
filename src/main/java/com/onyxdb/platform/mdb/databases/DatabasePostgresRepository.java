package com.onyxdb.platform.mdb.databases;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.dao.DuplicateKeyException;

import com.onyxdb.platform.generated.jooq.Indexes;
import com.onyxdb.platform.generated.jooq.tables.records.DatabasesRecord;
import com.onyxdb.platform.mdb.clusters.models.Database;
import com.onyxdb.platform.mdb.exceptions.BadRequestException;
import com.onyxdb.platform.mdb.exceptions.DatabaseNotFoundException;
import com.onyxdb.platform.mdb.utils.PsqlUtils;
import com.onyxdb.platform.mdb.utils.TimeUtils;

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
    public Optional<Database> getDatabaseO(UUID clusterId, String databaseName) {
        return dslContext.select()
                .from(DATABASES)
                .where(DATABASES.CLUSTER_ID.eq(clusterId)
                        .and(DATABASES.NAME.eq(databaseName))
                        .and(DATABASES.IS_DELETED.eq(false))
                )
                .fetchOptional()
                .map(r -> databaseMapper.map(r.into(DatabasesRecord.class)));
    }

    @Override
    public Database getDatabase(UUID clusterId, String databaseName) {
        return getDatabaseO(clusterId, databaseName).orElseThrow(() -> new DatabaseNotFoundException(databaseName));
    }

    @Override
    public void createDatabase(Database database) {
        try {
            dslContext.insertInto(DATABASES)
                    .set(databaseMapper.map(database))
                    .execute();
        } catch (DataAccessException | DuplicateKeyException e) {
            PsqlUtils.handleDataAccessEx(
                    e,
                    DATABASES,
                    Indexes.DATABASES_DATABASE_NAME_CLUSTER_ID_IS_DELETED_UNIQ_IDX,
                    () -> new BadRequestException(String.format(
                            "Database with name '%s' already exists", database.name()
                    ))
            );

            throw e;
        }
    }

    @Override
    public void markDatabaseAsDeleted(String databaseName, UUID deletedBy) {
        dslContext.update(DATABASES)
                .set(DATABASES.IS_DELETED, true)
                .set(DATABASES.DELETED_AT, TimeUtils.now())
                .set(DATABASES.DELETED_BY, deletedBy)
                .where(DATABASES.NAME.eq(databaseName))
                .execute();
    }
}
