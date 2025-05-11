package com.onyxdb.platform.idm.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.generated.jooq.Tables;
import com.onyxdb.platform.generated.jooq.tables.PermissionTable;
import com.onyxdb.platform.generated.jooq.tables.ProductTable;
import com.onyxdb.platform.generated.jooq.tables.RolePermissionTable;
import com.onyxdb.platform.generated.jooq.tables.RoleTable;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.Permission;
import com.onyxdb.platform.idm.models.Role;

import static org.jooq.impl.DSL.noTable;
import static org.jooq.impl.DSL.trueCondition;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class RolePostgresRepository implements RoleRepository {
    private final static RoleTable roleTable = Tables.ROLE_TABLE;
    private final static RolePermissionTable roleToPermissionTable = Tables.ROLE_PERMISSION_TABLE;
    private final static PermissionTable permissionTable = Tables.PERMISSION_TABLE;
    private final static ProductTable productTable = Tables.PRODUCT_TABLE;
    private final DSLContext dslContext;

    @Override
    public Optional<Role> findById(UUID id) {
        return dslContext.selectFrom(roleTable)
                .where(roleTable.ID.eq(id))
                .fetchOptional(Role::fromDAO);
    }

    @Override
    public PaginatedResult<Role> findAll(String query, UUID productId, UUID orgUnitId, Integer limit, Integer offset) {
        limit = (limit != null) ? limit : Integer.MAX_VALUE;
        offset = (offset != null) ? offset : 0;

        Condition condition = trueCondition();
        var table = roleTable.leftJoin(noTable()).on();
        if (productId != null) {
            condition = condition.and(roleTable.PRODUCT_ID.eq(productId));
        }
        if (orgUnitId != null) {
            condition = condition.and(roleTable.ORG_UNIT_ID.eq(orgUnitId));
        }
        if (query != null && !query.isEmpty()) {
            table = roleTable.leftJoin(productTable).on(roleTable.PRODUCT_ID.eq(productTable.ID));
            condition = condition.and(
                    roleTable.NAME.containsIgnoreCase(query)
                            .or(roleTable.SHOP_NAME.containsIgnoreCase(query))
                            .or(roleTable.DESCRIPTION.containsIgnoreCase(query))
                            .or(productTable.NAME.containsIgnoreCase(query))
                            .or(productTable.DESCRIPTION.containsIgnoreCase(query)));
        }

        Result<Record> records = dslContext.selectFrom(table)
                .where(condition)
                .orderBy(roleTable.CREATED_AT)
                .limit(limit)
                .offset(offset)
                .fetch();
        List<Role> data = records.map(record -> Role.fromDAO(record.into(roleTable)));

        int totalCount = dslContext.selectCount()
                .from(table)
                .where(condition)
                .fetchOptional()
                .map(record -> record.get(0, int.class))
                .orElse(0);

        return new PaginatedResult<>(
                data,
                totalCount,
                offset + 1,
                Math.min(offset + limit, totalCount)
        );
    }

    @Override
    public Role create(Role role) {
        String entity = role.entity();
        if (entity == null || entity.isEmpty()) {
            if (role.productId() != null) {
                entity = "product";
            } else if (role.orgUnitId() != null) {
                entity = "orgunit";
            }
        }
        var record = dslContext.insertInto(roleTable)
                .set(roleTable.ID, UUID.randomUUID())
                .set(roleTable.NAME, role.name())
                .set(roleTable.SHOP_NAME, role.name())
                .set(roleTable.DESCRIPTION, role.description())
                .set(roleTable.ENTITY, entity)
                .set(roleTable.ROLE_TYPE, role.roleType())
                .set(roleTable.IS_SHOP_HIDDEN, role.isShopHidden())
                .set(roleTable.PRODUCT_ID, role.productId())
                .set(roleTable.ORG_UNIT_ID, role.orgUnitId())
                .set(roleTable.CREATED_AT, LocalDateTime.now())
                .set(roleTable.UPDATED_AT, LocalDateTime.now())
                .returning()
                .fetchOne();

        assert record != null;
        return Role.fromDAO(record);
    }

    @Override
    public Role update(Role role) {
        String entity = role.entity();
        if (entity == null || entity.isEmpty()) {
            if (role.productId() != null) {
                entity = "product";
            } else if (role.orgUnitId() != null) {
                entity = "orgunit";
            }
        }
        var record = dslContext.update(roleTable)
                .set(roleTable.NAME, role.name())
                .set(roleTable.SHOP_NAME, role.name())
                .set(roleTable.DESCRIPTION, role.description())
                .set(roleTable.ENTITY, entity)
                .set(roleTable.ROLE_TYPE, role.roleType())
                .set(roleTable.IS_SHOP_HIDDEN, role.isShopHidden())
                .set(roleTable.PRODUCT_ID, role.productId())
                .set(roleTable.ORG_UNIT_ID, role.orgUnitId())
                .set(roleTable.UPDATED_AT, LocalDateTime.now())
                .where(roleTable.ID.eq(role.id()))
                .returning()
                .fetchOne();

        assert record != null;
        return Role.fromDAO(record);
    }

    @Override
    public void delete(UUID roleId) {
        List<UUID> permissionIds = dslContext.select(roleToPermissionTable.PERMISSION_ID)
                .from(roleToPermissionTable)
                .where(roleToPermissionTable.ROLE_ID.eq(roleId))
                .fetch(roleToPermissionTable.PERMISSION_ID);

        if (!permissionIds.isEmpty()) {
            dslContext.deleteFrom(permissionTable)
                    .where(permissionTable.ID.in(permissionIds))
                    .execute();
        }

        dslContext.deleteFrom(roleToPermissionTable)
                .where(roleToPermissionTable.ROLE_ID.eq(roleId))
                .execute();

        dslContext.deleteFrom(roleTable)
                .where(roleTable.ID.eq(roleId))
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
    public List<Permission> getPermissions(UUID roleId) {
        return dslContext.selectFrom(roleToPermissionTable)
                .where(roleToPermissionTable.ROLE_ID.eq(roleId))
                .fetch(link -> dslContext.selectFrom(permissionTable)
                        .where(permissionTable.ID.eq(link.getPermissionId()))
                        .fetchOne(Permission::fromDAO)
                );
    }
}