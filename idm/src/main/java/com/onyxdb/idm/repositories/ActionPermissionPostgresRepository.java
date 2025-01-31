package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.ActionPermissionTable;
import com.onyxdb.idm.models.ActionPermission;

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
public class ActionPermissionPostgresRepository implements ActionPermissionRepository {
    private final DSLContext dslContext;
    private final static ActionPermissionTable actionPermissionTable = Tables.ACTION_PERMISSION_TABLE;

    @Override
    public Optional<ActionPermission> findById(UUID id) {
        return dslContext.selectFrom(actionPermissionTable)
                .where(actionPermissionTable.ID.eq(id))
                .fetchOptional(ActionPermission::fromDAO);
    }

    @Override
    public List<ActionPermission> findAll() {
        return dslContext.selectFrom(actionPermissionTable)
                .fetch(ActionPermission::fromDAO);
    }

    @Override
    public void create(ActionPermission actionPermission) {
        dslContext.insertInto(actionPermissionTable)
                .set(actionPermissionTable.ID, actionPermission.id())
                .set(actionPermissionTable.ACTION_TYPE, actionPermission.actionType())
                .set(actionPermissionTable.RESOURCE_FIELDS, actionPermission.resourceFields().toArray(String[]::new))
                .set(actionPermissionTable.LABELS, actionPermission.labels().toArray(String[]::new))
                .set(actionPermissionTable.CREATED_AT, actionPermission.createdAt())
                .set(actionPermissionTable.UPDATED_AT, actionPermission.updatedAt())
                .execute();
    }

    @Override
    public void update(ActionPermission actionPermission) {
        dslContext.update(actionPermissionTable)
                .set(actionPermissionTable.ACTION_TYPE, actionPermission.actionType())
                .set(actionPermissionTable.RESOURCE_FIELDS, actionPermission.resourceFields().toArray(String[]::new))
                .set(actionPermissionTable.LABELS, actionPermission.labels().toArray(String[]::new))
                .set(actionPermissionTable.UPDATED_AT, actionPermission.updatedAt())
                .where(actionPermissionTable.ID.eq(actionPermission.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(actionPermissionTable)
                .where(actionPermissionTable.ID.eq(id))
                .execute();
    }
}