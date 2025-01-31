package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.ActionPermissionTable;
import com.onyxdb.idm.generated.jooq.tables.ApiPermissionTable;
import com.onyxdb.idm.generated.jooq.tables.RoleActionPermissionTable;
import com.onyxdb.idm.generated.jooq.tables.RoleApiPermissionTable;
import com.onyxdb.idm.generated.jooq.tables.RoleTable;
import com.onyxdb.idm.models.ActionPermission;
import com.onyxdb.idm.models.ApiPermission;
import com.onyxdb.idm.models.Role;

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
public class RolePostgresRepository implements RoleRepository {
    private final DSLContext dslContext;
    private final static RoleTable roleTable = Tables.ROLE_TABLE;
    private final static RoleActionPermissionTable roleToActionPermissionTable = Tables.ROLE_ACTION_PERMISSION_TABLE;
    private final static RoleApiPermissionTable roleToApiPermissionTable = Tables.ROLE_API_PERMISSION_TABLE;
    private final static ActionPermissionTable actionPermissionTable = Tables.ACTION_PERMISSION_TABLE;
    private final static ApiPermissionTable apiPermissionTable = Tables.API_PERMISSION_TABLE;

    @Override
    public Optional<Role> findById(UUID id) {
        return dslContext.selectFrom(roleTable)
                .where(roleTable.ID.eq(id))
                .fetchOptional(Role::fromDAO);
    }

    @Override
    public List<Role> findAll() {
        return dslContext.selectFrom(roleTable)
                .fetch(Role::fromDAO);
    }

    @Override
    public void create(Role role) {
        dslContext.insertInto(roleTable)
                .set(roleTable.ID, role.id())
                .set(roleTable.NAME, role.name())
                .set(roleTable.DESCRIPTION, role.description())
                .set(roleTable.RESOURCE_ID, role.resourceId())
                .set(roleTable.CREATED_AT, role.createdAt())
                .set(roleTable.UPDATED_AT, role.updatedAt())
                .execute();
    }

    @Override
    public void update(Role role) {
        dslContext.update(roleTable)
                .set(roleTable.NAME, role.name())
                .set(roleTable.DESCRIPTION, role.description())
                .set(roleTable.UPDATED_AT, role.updatedAt())
                .where(roleTable.ID.eq(role.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(roleTable)
                .where(roleTable.ID.eq(id))
                .execute();
    }

    @Override
    public void addActionPermission(UUID roleId, UUID permissionId) {
        dslContext.insertInto(roleToActionPermissionTable)
                .set(roleToActionPermissionTable.ROLE_ID, roleId)
                .set(roleToActionPermissionTable.ACTION_PERMISSION_ID, permissionId)
                .execute();
    }

    @Override
    public void removeActionPermission(UUID roleId, UUID permissionId) {
        dslContext.deleteFrom(roleToActionPermissionTable)
                .where(roleToActionPermissionTable.ROLE_ID.eq(roleId)
                        .and(roleToActionPermissionTable.ACTION_PERMISSION_ID.eq(permissionId)))
                .execute();
    }

    @Override
    public List<ActionPermission> getActionPermissionsByRoleId(UUID roleId) {
        return dslContext.selectFrom(roleToActionPermissionTable)
                .where(roleToActionPermissionTable.ROLE_ID.eq(roleId))
                .fetch(link -> dslContext.selectFrom(actionPermissionTable)
                        .where(actionPermissionTable.ID.eq(link.getActionPermissionId()))
                        .fetchOne(ActionPermission::fromDAO)
                );
    }

    @Override
    public void addApiPermission(UUID roleId, UUID permissionId) {
        dslContext.insertInto(roleToApiPermissionTable)
                .set(roleToApiPermissionTable.ROLE_ID, roleId)
                .set(roleToApiPermissionTable.API_PERMISSION_ID, permissionId)
                .execute();
    }

    @Override
    public void removeApiPermission(UUID roleId, UUID permissionId) {
        dslContext.deleteFrom(roleToApiPermissionTable)
                .where(roleToApiPermissionTable.ROLE_ID.eq(roleId)
                        .and(roleToApiPermissionTable.API_PERMISSION_ID.eq(permissionId)))
                .execute();
    }

    @Override
    public List<ApiPermission> getApiPermissionsByRoleId(UUID roleId) {
        return dslContext.selectFrom(roleToApiPermissionTable)
                .where(roleToApiPermissionTable.ROLE_ID.eq(roleId))
                .fetch(link -> dslContext.selectFrom(apiPermissionTable)
                        .where(apiPermissionTable.ID.eq(link.getApiPermissionId()))
                        .fetchOne(ApiPermission::fromDAO)
                );
    }
}
