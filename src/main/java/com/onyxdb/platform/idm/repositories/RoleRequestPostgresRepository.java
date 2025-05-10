package com.onyxdb.platform.idm.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.generated.jooq.Tables;
import com.onyxdb.platform.generated.jooq.tables.AccountTable;
import com.onyxdb.platform.generated.jooq.tables.RoleRequestTable;
import com.onyxdb.platform.generated.jooq.tables.RoleTable;
import com.onyxdb.platform.generated.jooq.tables.records.AccountTableRecord;
import com.onyxdb.platform.generated.jooq.tables.records.RoleTableRecord;
import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.Role;
import com.onyxdb.platform.idm.models.RoleRequest;
import com.onyxdb.platform.idm.models.RoleRequestFull;

import static org.jooq.impl.DSL.trueCondition;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class RoleRequestPostgresRepository implements RoleRequestRepository {
    private final static RoleRequestTable roleRequestTable = Tables.ROLE_REQUEST_TABLE;
    private final static RoleTable roleTable = Tables.ROLE_TABLE;
    private final static AccountTable accountTable = Tables.ACCOUNT_TABLE;
    private final DSLContext dslContext;

    @Override
    public Optional<RoleRequest> findById(UUID id) {
        return dslContext.selectFrom(roleRequestTable)
                .where(roleRequestTable.ID.eq(id))
                .fetchOptional(RoleRequest::fromDAO);
    }

    @Override
    public PaginatedResult<RoleRequestFull> findAll(String status, UUID accountId, UUID ownerId, UUID roleId, Integer limit, Integer offset) {
        limit = (limit != null) ? limit : Integer.MAX_VALUE;
        offset = (offset != null) ? offset : 0;

        Condition condition = trueCondition();
        if (status != null && !status.isEmpty()) {
            condition = condition.and(roleRequestTable.STATUS.eq(status));
        }
        if (ownerId != null) {
            condition = condition.and(roleRequestTable.OWNER_ID.eq(ownerId));
        }
        if (accountId != null) {
            condition = condition.and(roleRequestTable.ACCOUNT_ID.eq(accountId));
        }
        if (roleId != null) {
            condition = condition.and(roleRequestTable.ROLE_ID.eq(roleId));
        }

        AccountTable accountRec = accountTable.as("receiver");
        AccountTable accountOwn = accountTable.as("owner");

        List<RoleRequestFull> data = dslContext
                .select(
                        roleRequestTable.ID,
                        roleRequestTable.REASON,
                        roleRequestTable.STATUS,
                        roleRequestTable.CREATED_AT,
                        roleRequestTable.RESOLVED_AT,

                        // Role fields
                        roleTable.ID.as("role.id"),
                        roleTable.ROLE_TYPE.as("role.role_type"),
                        roleTable.NAME.as("role.name"),
                        roleTable.SHOP_NAME.as("role.shop_name"),
                        roleTable.DESCRIPTION.as("role.description"),
                        roleTable.ENTITY.as("role.entity"),
                        roleTable.PRODUCT_ID.as("role.product_id"),
                        roleTable.ORG_UNIT_ID.as("role.org_unit_id"),
                        roleTable.IS_SHOP_HIDDEN.as("role.is_shop_hidden"),
                        roleTable.CREATED_AT.as("role.created_at"),
                        roleTable.UPDATED_AT.as("role.updated_at"),

                        // Account fields (requesting user)
                        accountRec.ID.as("account.id"),
                        accountRec.LOGIN.as("account.login"),
                        accountRec.EMAIL.as("account.email"),
                        accountRec.FIRST_NAME.as("account.first_name"),
                        accountRec.LAST_NAME.as("account.last_name"),
                        accountRec.DATA.as("account.data"),
                        accountRec.CREATED_AT.as("account.created_at"),
                        accountRec.UPDATED_AT.as("account.updated_at"),

                        // Owner fields (approver)
                        accountOwn.ID.as("owner.id"),
                        accountOwn.LOGIN.as("owner.login"),
                        accountOwn.EMAIL.as("owner.email"),
                        accountOwn.FIRST_NAME.as("owner.first_name"),
                        accountOwn.LAST_NAME.as("owner.last_name"),
                        accountOwn.DATA.as("owner.data"),
                        accountOwn.CREATED_AT.as("owner.created_at"),
                        accountOwn.UPDATED_AT.as("owner.updated_at")
                )
                .from(roleRequestTable)
                .join(roleTable).on(roleRequestTable.ROLE_ID.eq(roleTable.ID))
                .join(accountRec).on(roleRequestTable.ACCOUNT_ID.eq(accountRec.ID))
                .join(accountOwn).on(roleRequestTable.OWNER_ID.eq(accountOwn.ID))
                .where(condition)
                .orderBy(roleRequestTable.CREATED_AT.desc())
                .limit(limit)
                .offset(offset)
                .fetch(record -> {
                    var role = new RoleTableRecord(
                            record.get("role.id", UUID.class),
                            record.get("role.role_type", String.class),
                            record.get("role.name", String.class),
                            record.get("role.shop_name", String.class),
                            record.get("role.description", String.class),
                            record.get("role.entity", String.class),
                            record.get("role.product_id", UUID.class),
                            record.get("role.org_unit_id", UUID.class),
                            record.get("role.is_shop_hidden", Boolean.class),
                            record.get("role.created_at", LocalDateTime.class),
                            record.get("role.updated_at", LocalDateTime.class)
                    );

                    var account = new AccountTableRecord(
                            record.get("account.id", UUID.class),
                            record.get("account.login", String.class),
                            null, // password should not be exposed
                            record.get("account.email", String.class),
                            record.get("account.first_name", String.class),
                            record.get("account.last_name", String.class),
                            record.get("account.data", JSONB.class),
                            record.get("account.created_at", LocalDateTime.class),
                            record.get("account.updated_at", LocalDateTime.class)
                    );

                    var owner = new AccountTableRecord(
                            record.get("owner.id", UUID.class),
                            record.get("owner.login", String.class),
                            null, // password should not be exposed
                            record.get("owner.email", String.class),
                            record.get("owner.first_name", String.class),
                            record.get("owner.last_name", String.class),
                            record.get("owner.data", JSONB.class),
                            record.get("owner.created_at", LocalDateTime.class),
                            record.get("owner.updated_at", LocalDateTime.class)
                    );

                    return new RoleRequestFull(
                            record.get(roleRequestTable.ID),
                            Role.fromDAO(role),
                            Account.fromDAO(account),
                            Account.fromDAO(owner),
                            record.get(roleRequestTable.REASON),
                            record.get(roleRequestTable.STATUS),
                            record.get(roleRequestTable.CREATED_AT),
                            record.get(roleRequestTable.RESOLVED_AT)
                    );
                });

        int totalCount = dslContext.fetchCount(roleRequestTable, condition);
        return new PaginatedResult<>(
                data,
                totalCount,
                offset + 1,
                Math.min(offset + limit, totalCount)
        );
    }

//    @Override
//    public PaginatedResult<RoleRequestFull> search(String query, Integer limit, Integer offset) {
//        if (query == null || query.isEmpty()) {
//            return findAll(null, null, null, null, limit, offset);
//        }
//        limit = (limit != null) ? limit : Integer.MAX_VALUE;
//        offset = (offset != null) ? offset : 0;
//
//        Condition condition = roleTable.NAME.containsIgnoreCase(query)
//                .or(accountTable.FIRST_NAME.containsIgnoreCase(query))
//                .or(accountTable.LAST_NAME.containsIgnoreCase(query));
//
//        var table = roleRequestTable
//                .leftJoin(roleTable).on(roleRequestTable.ROLE_ID.eq(roleTable.ID))
//                .leftJoin(accountTable).on(roleRequestTable.ACCOUNT_ID.eq(accountTable.ID));
//        Result<Record> records = dslContext.selectFrom(table)
//                .where(condition)
//                .limit(limit)
//                .offset(offset)
//                .fetch();
//
//        List<RoleRequestFull> data = records.map(record -> RoleRequestFull.fromDAO(record.into(roleRequestTable)));
//
//        int totalCount = dslContext.selectCount()
//                .from(table)
//                .where(condition)
//                .fetchOptional()
//                .map(record -> record.get(0, int.class))
//                .orElse(0);
//        return new PaginatedResult<>(
//                data,
//                totalCount,
//                offset + 1,
//                Math.min(offset + limit, totalCount)
//        );
//    }

    @Override
    public RoleRequest create(RoleRequest roleRequest) {
        var record = dslContext.insertInto(roleRequestTable)
                .set(roleRequestTable.ID, UUID.randomUUID())
                .set(roleRequestTable.ROLE_ID, roleRequest.roleId())
                .set(roleRequestTable.ACCOUNT_ID, roleRequest.accountId())
                .set(roleRequestTable.REASON, roleRequest.reason())
                .set(roleRequestTable.OWNER_ID, roleRequest.ownerId())
                .set(roleRequestTable.STATUS, roleRequest.status())
                .set(roleRequestTable.CREATED_AT, LocalDateTime.now())
                .returning()
                .fetchOne();

        assert record != null;
        return RoleRequest.fromDAO(record);
    }

    @Override
    public RoleRequest update(RoleRequest roleRequest) {
        var record = dslContext.update(roleRequestTable)
                .set(roleRequestTable.ROLE_ID, roleRequest.roleId())
                .set(roleRequestTable.ACCOUNT_ID, roleRequest.accountId())
                .set(roleRequestTable.REASON, roleRequest.reason())
                .set(roleRequestTable.OWNER_ID, roleRequest.ownerId())
                .set(roleRequestTable.STATUS, roleRequest.status())
                .set(roleRequestTable.RESOLVED_AT, roleRequest.resolvedAt())
                .where(roleRequestTable.ID.eq(roleRequest.id()))
                .returning()
                .fetchOne();

        assert record != null;
        return RoleRequest.fromDAO(record);
    }

    @Override
    public RoleRequest setStatus(UUID id, String status) {
        var record = dslContext.update(roleRequestTable)
                .set(roleRequestTable.STATUS, status)
                .set(roleRequestTable.RESOLVED_AT, LocalDateTime.now())
                .where(roleRequestTable.ID.eq(id))
                .returning()
                .fetchOne();

        assert record != null;
        return RoleRequest.fromDAO(record);
    }
}
