package com.onyxdb.idm.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.PermissionTable;
import com.onyxdb.idm.models.Permission;

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
    public Permission create(Permission permission) {
        var record = dslContext.insertInto(permissionTable)
                .set(permissionTable.ID, UUID.randomUUID())
                .set(permissionTable.ACTION_TYPE, permission.actionType())
                .set(permissionTable.RESOURCE_TYPE, permission.resourceType())
                .set(permissionTable.DATA, permission.getDataAsJsonb())
                .set(permissionTable.CREATED_AT, LocalDateTime.now())
                .set(permissionTable.UPDATED_AT, LocalDateTime.now())
                .returning()
                .fetchOne();

        assert record != null;
        return Permission.fromDAO(record);
    }

    @Override
    public Permission update(Permission permission) {
        var record = dslContext.update(permissionTable)
                .set(permissionTable.ACTION_TYPE, permission.actionType())
                .set(permissionTable.RESOURCE_TYPE, permission.resourceType())
                .set(permissionTable.DATA, permission.getDataAsJsonb())
                .set(permissionTable.UPDATED_AT, LocalDateTime.now())
                .where(permissionTable.ID.eq(permission.id()))
                .returning()
                .fetchOne();

        assert record != null;
        return Permission.fromDAO(record);
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(permissionTable)
                .where(permissionTable.ID.eq(id))
                .execute();
    }
}