package com.onyxdb.idm.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.AccountBusinessRoleTable;
import com.onyxdb.idm.generated.jooq.tables.AccountOuTable;
import com.onyxdb.idm.generated.jooq.tables.AccountRoleTable;
import com.onyxdb.idm.generated.jooq.tables.AccountTable;
import com.onyxdb.idm.generated.jooq.tables.BusinessRoleRoleTable;
import com.onyxdb.idm.generated.jooq.tables.BusinessRoleTable;
import com.onyxdb.idm.generated.jooq.tables.OrganizationUnitTable;
import com.onyxdb.idm.generated.jooq.tables.PermissionTable;
import com.onyxdb.idm.generated.jooq.tables.ProductTable;
import com.onyxdb.idm.generated.jooq.tables.RolePermissionTable;
import com.onyxdb.idm.generated.jooq.tables.RoleTable;
import com.onyxdb.idm.models.Permission;

import static org.jooq.impl.DSL.noCondition;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class PermissionPostgresRepository implements PermissionRepository {
    private final static AccountTable accountTable = Tables.ACCOUNT_TABLE;
    private final static AccountBusinessRoleTable accountBusinessRoleTable = Tables.ACCOUNT_BUSINESS_ROLE_TABLE;
    private final static AccountRoleTable accountRoleTable = Tables.ACCOUNT_ROLE_TABLE;
    private final static BusinessRoleTable businessRoleTable = Tables.BUSINESS_ROLE_TABLE;
    private final static BusinessRoleRoleTable businessRoleToRoleTable = Tables.BUSINESS_ROLE_ROLE_TABLE;
    private final static RolePermissionTable rolePermissionTable = Tables.ROLE_PERMISSION_TABLE;
    private final static RoleTable roleTable = Tables.ROLE_TABLE;
    private final static PermissionTable permissionTable = Tables.PERMISSION_TABLE;
    private final static ProductTable productTable = Tables.PRODUCT_TABLE;
    private final static OrganizationUnitTable organizationUnitTable = Tables.ORGANIZATION_UNIT_TABLE;
    private final static AccountOuTable organizationUnitAccountTable = Tables.ACCOUNT_OU_TABLE;

    private final DSLContext dslContext;


    @Override
    public Optional<Permission> findById(UUID id) {
        return dslContext.selectFrom(permissionTable)
                .where(permissionTable.ID.eq(id))
                .fetchOptional(Permission::fromDAO);
    }

    @Override
    public List<Permission> findAll() {
        return dslContext.selectFrom(permissionTable)
                .orderBy(permissionTable.CREATED_AT)
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

    @Override
    public List<Permission> findAccountPermissionsToProduct(UUID accountId, UUID productId, String permissionType) {
        return dslContext.select(permissionTable.fields())
                .from(permissionTable)
                .join(rolePermissionTable).on(permissionTable.ID.eq(rolePermissionTable.PERMISSION_ID))
                .join(roleTable).on(rolePermissionTable.ROLE_ID.eq(roleTable.ID))
                .join(accountRoleTable).on(roleTable.ID.eq(accountRoleTable.ROLE_ID))
                .where(accountRoleTable.ACCOUNT_ID.eq(accountId))
                .and(roleTable.PRODUCT_ID.eq(productId))
                .and(permissionType == null ? noCondition() : permissionTable.ACTION_TYPE.eq(permissionType))
                .fetch(record -> Permission.fromDAO(record.into(permissionTable)));
    }

    @Override
    public List<Permission> findAccountPermissionsToOrgUnit(UUID accountId, UUID orgUnitId, String permissionType) {
        return dslContext.select(permissionTable.fields())
                .from(permissionTable)
                .join(rolePermissionTable).on(permissionTable.ID.eq(rolePermissionTable.PERMISSION_ID))
                .join(roleTable).on(rolePermissionTable.ROLE_ID.eq(roleTable.ID))
                .join(accountRoleTable).on(roleTable.ID.eq(accountRoleTable.ROLE_ID))
                .where(accountRoleTable.ACCOUNT_ID.eq(accountId))
                .and(roleTable.ORG_UNIT_ID.eq(orgUnitId))
                .and(permissionType == null ? noCondition() : permissionTable.ACTION_TYPE.eq(permissionType))
                .orderBy(permissionTable.CREATED_AT)
                .fetch(record -> Permission.fromDAO(record.into(permissionTable)));
    }

    @Override
    public List<Permission> findAllAccountPermissions(UUID accountId, String resourceType) {
        return dslContext.select(permissionTable.fields())
                .from(permissionTable)
                .join(rolePermissionTable).on(permissionTable.ID.eq(rolePermissionTable.PERMISSION_ID))
                .join(roleTable).on(rolePermissionTable.ROLE_ID.eq(roleTable.ID))
                .join(accountRoleTable).on(roleTable.ID.eq(accountRoleTable.ROLE_ID))
                .where(accountRoleTable.ACCOUNT_ID.eq(accountId))
                .and(resourceType == null ? noCondition() : permissionTable.RESOURCE_TYPE.eq(resourceType))
                .orderBy(permissionTable.CREATED_AT)
                .fetch(record -> Permission.fromDAO(record.into(permissionTable)));
    }

    @Override
    public List<Permission> findAccountPermissionsViaBusinessRoles(UUID accountId, String resourceType, String permissionType) {
        return dslContext.select(permissionTable.fields())
                .from(permissionTable)
                .join(rolePermissionTable).on(permissionTable.ID.eq(rolePermissionTable.PERMISSION_ID))
                .join(roleTable).on(rolePermissionTable.ROLE_ID.eq(roleTable.ID))
                .join(businessRoleToRoleTable).on(roleTable.ID.eq(businessRoleToRoleTable.ROLE_ID))
                .join(businessRoleTable).on(businessRoleToRoleTable.BUSINESS_ROLE_ID.eq(businessRoleTable.ID))
                .join(accountBusinessRoleTable).on(businessRoleTable.ID.eq(accountBusinessRoleTable.BUSINESS_ROLE_ID))
                .where(accountBusinessRoleTable.ACCOUNT_ID.eq(accountId))
                .and(resourceType == null ? noCondition() : permissionTable.RESOURCE_TYPE.eq(resourceType))
                .and(permissionType == null ? noCondition() : permissionTable.ACTION_TYPE.eq(permissionType))
                .orderBy(permissionTable.CREATED_AT)
                .fetch(record -> Permission.fromDAO(record.into(permissionTable)));
    }
}