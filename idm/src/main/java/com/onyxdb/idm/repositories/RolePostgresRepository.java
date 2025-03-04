package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.PermissionTable;
import com.onyxdb.idm.generated.jooq.tables.ProductTable;
import com.onyxdb.idm.generated.jooq.tables.RolePermissionTable;
import com.onyxdb.idm.generated.jooq.tables.RoleTable;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.Permission;
import com.onyxdb.idm.models.Role;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.jooq.impl.DSL.noTable;
import static org.jooq.impl.DSL.trueCondition;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class RolePostgresRepository implements RoleRepository {
    private final DSLContext dslContext;
    private final static RoleTable roleTable = Tables.ROLE_TABLE;
    private final static RolePermissionTable roleToPermissionTable = Tables.ROLE_PERMISSION_TABLE;
    private final static PermissionTable permissionTable = Tables.PERMISSION_TABLE;
    private final static ProductTable productTable = Tables.PRODUCT_TABLE;

    @Override
    public Optional<Role> findById(UUID id) {
        return dslContext.selectFrom(roleTable)
                .where(roleTable.ID.eq(id))
                .fetchOptional(Role::fromDAO);
    }

    @Override
    public PaginatedResult<Role> findAll(String query, UUID productId, UUID orgUnitId, int limit, int offset) {
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
            condition = roleTable.ID.eq(UUID.fromString(query))
                    .or(roleTable.NAME.containsIgnoreCase(query))
                    .or(roleTable.SHOP_NAME.containsIgnoreCase(query))
                    .or(roleTable.DESCRIPTION.containsIgnoreCase(query))
                    .or(productTable.NAME.containsIgnoreCase(query))
                    .or(productTable.DESCRIPTION.containsIgnoreCase(query));
        }

        Result<Record> records = dslContext.selectFrom(table)
                .where(condition)
                .limit(limit)
                .offset(offset)
                .fetch();
        List<Role> data = records.map(record -> Role.fromDAO(record.into(roleTable)));

        int totalCount = dslContext.fetchCount(roleTable, condition);

        int startPosition = offset + 1;
        int endPosition = Math.min(offset + limit, totalCount);

        return new PaginatedResult<>(data, totalCount, startPosition, endPosition);
    }

    @Override
    public void create(Role role) {
        dslContext.insertInto(roleTable)
                .set(roleTable.ID, role.id())
                .set(roleTable.NAME, role.name())
                .set(roleTable.DESCRIPTION, role.description())
                .set(roleTable.PRODUCT_ID, role.productId())
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
    public List<Permission> getPermissions(UUID roleId) {
        return dslContext.selectFrom(roleToPermissionTable)
                .where(roleToPermissionTable.ROLE_ID.eq(roleId))
                .fetch(link -> dslContext.selectFrom(permissionTable)
                        .where(permissionTable.ID.eq(link.getPermissionId()))
                        .fetchOne(Permission::fromDAO)
                );
    }
}