package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.PermissionTable;
import com.onyxdb.idm.models.Permission;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class PermissionPostgresRepository implements PermissionRepository {
    private final DSLContext dslContext;
    private final static PermissionTable permissionTable = Tables.PERMISSION_TABLE;

    @Override
    public Optional<Permission> findById(UUID id) {
        return dslContext.selectFrom(permissionTable)
                .where(permissionTable.ID.eq(id))
                .fetchOptional(Permission::fromDAO);
    }

    @Override
    public List<Permission> findAll() {
        return dslContext.selectFrom(permissionTable)
                .fetch(Permission::fromDAO);
    }

    @Override
    public void create(Permission permission) {
        dslContext.insertInto(permissionTable)
                .set(permissionTable.ID, permission.id())
                .set(permissionTable.ACTION_TYPE, permission.actionType())
                .set(permissionTable.DATA, permission.getDataAsJsonb())
                .set(permissionTable.CREATED_AT, permission.createdAt())
                .set(permissionTable.UPDATED_AT, permission.updatedAt())
                .execute();
    }

    @Override
    public void update(Permission permission) {
        dslContext.update(permissionTable)
                .set(permissionTable.ACTION_TYPE, permission.actionType())
                .set(permissionTable.DATA, permission.getDataAsJsonb())
                .set(permissionTable.UPDATED_AT, permission.updatedAt())
                .where(permissionTable.ID.eq(permission.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(permissionTable)
                .where(permissionTable.ID.eq(id))
                .execute();
    }
}