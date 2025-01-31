package com.onyxdb.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.ApiPermissionTable;
import com.onyxdb.idm.models.ApiPermission;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class ApiPermissionPostgresRepository implements ApiPermissionRepository {
    private final DSLContext dslContext;
    private final static ApiPermissionTable ApiPermissionTable = Tables.API_PERMISSION_TABLE;

    @Override
    public Optional<ApiPermission> findById(UUID id) {
        return dslContext.selectFrom(ApiPermissionTable)
                .where(ApiPermissionTable.ID.eq(id))
                .fetchOptional(ApiPermission::fromDAO);
    }

    @Override
    public List<ApiPermission> findAll() {
        return dslContext.selectFrom(ApiPermissionTable)
                .fetch(ApiPermission::fromDAO);
    }

    @Override
    public void create(ApiPermission ApiPermission) {
        dslContext.insertInto(ApiPermissionTable)
                .set(ApiPermissionTable.ID, ApiPermission.id())
                .set(ApiPermissionTable.API_PATH_REGEXP, ApiPermission.apiPathRegexp())
                .set(ApiPermissionTable.CREATED_AT, ApiPermission.createdAt())
                .set(ApiPermissionTable.UPDATED_AT, ApiPermission.updatedAt())
                .execute();
    }

    @Override
    public void update(ApiPermission ApiPermission) {
        dslContext.update(ApiPermissionTable)
                .set(ApiPermissionTable.API_PATH_REGEXP, ApiPermission.apiPathRegexp())
                .set(ApiPermissionTable.UPDATED_AT, ApiPermission.updatedAt())
                .where(ApiPermissionTable.ID.eq(ApiPermission.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(ApiPermissionTable)
                .where(ApiPermissionTable.ID.eq(id))
                .execute();
    }
}