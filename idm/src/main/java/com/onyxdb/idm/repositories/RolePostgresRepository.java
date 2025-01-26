package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.PermissionTable;
import com.onyxdb.idm.generated.jooq.tables.RolePermissionTable;
import com.onyxdb.idm.generated.jooq.tables.RoleTable;
import com.onyxdb.idm.models.Permission;
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
    private final RoleTable roleTable = Tables.ROLE_TABLE;
    private final RolePermissionTable roleToPermissionTable = Tables.ROLE_PERMISSION_TABLE;
    private final PermissionTable permissionTable = Tables.PERMISSION_TABLE;

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
    public void addPermission(UUID roleId, UUID permissionId) {
        dslContext.insertInto(roleToPermissionTable)
                .set(roleToPermissionTable.ROLE_ID, roleId)
                .set(roleToPermissionTable.PERMISSION_ID, permissionId)
                .execute();
    }

    @Override
    public void removePermission(UUID roleId, UUID permissionId) {
        dslContext.deleteFrom(roleToPermissionTable)
                .where(roleToPermissionTable.ROLE_ID.eq(roleId)
                        .and(roleToPermissionTable.PERMISSION_ID.eq(permissionId)))
                .execute();
    }

    @Override
    public List<Permission> getPermissionsByRoleId(UUID roleId) {
        return dslContext.selectFrom(roleToPermissionTable)
                .where(roleToPermissionTable.ROLE_ID.eq(roleId))
                .fetch(link -> dslContext.selectFrom(permissionTable)
                        .where(permissionTable.ID.eq(link.getPermissionId()))
                        .fetchOne(Permission::fromDAO)
                );
    }
}