package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.PermissionTable;
import com.onyxdb.idm.models.PermissionDTO;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PermissionPostgresRepository implements PermissionRepository {

    private final DSLContext dslContext;
    private final PermissionTable permissionTable = Tables.PERMISSION_TABLE;

    @Override
    public Optional<PermissionDTO> findById(UUID id) {
        return dslContext.selectFrom(permissionTable)
                .where(permissionTable.ID.eq(id))
                .fetchOptional(record -> PermissionDTO.builder()
                        .id(record.getId())
                        .actionType(record.getActionType())
                        .resourceType(record.getResourceType())
                        .resourceFields(record.getResourceFields())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }

    @Override
    public List<PermissionDTO> findAll() {
        return dslContext.selectFrom(permissionTable)
                .fetch(record -> PermissionDTO.builder()
                        .id(record.getId())
                        .actionType(record.getActionType())
                        .resourceType(record.getResourceType())
                        .resourceFields(record.getResourceFields())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }

    @Override
    public void create(PermissionDTO permission) {
        dslContext.insertInto(permissionTable)
                .set(permissionTable.ID, permission.getId())
                .set(permissionTable.ACTION_TYPE, permission.getActionType())
                .set(permissionTable.RESOURCE_TYPE, permission.getResourceType())
                .set(permissionTable.RESOURCE_FIELDS, permission.getResourceFields())
                .set(permissionTable.CREATED_AT, permission.getCreatedAt())
                .set(permissionTable.UPDATED_AT, permission.getUpdatedAt())
                .execute();
    }

    @Override
    public void update(PermissionDTO permission) {
        dslContext.update(permissionTable)
                .set(permissionTable.ACTION_TYPE, permission.getActionType())
                .set(permissionTable.RESOURCE_TYPE, permission.getResourceType())
                .set(permissionTable.RESOURCE_FIELDS, permission.getResourceFields())
                .set(permissionTable.UPDATED_AT, permission.getUpdatedAt())
                .where(permissionTable.ID.eq(permission.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(permissionTable)
                .where(permissionTable.ID.eq(id))
                .execute();
    }
}