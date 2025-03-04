package com.onyxdb.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.AccountTable;
import com.onyxdb.idm.generated.jooq.tables.RoleRequestTable;
import com.onyxdb.idm.generated.jooq.tables.RoleTable;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.RoleRequest;

import static org.jooq.impl.DSL.trueCondition;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class RoleRequestPostgresRepository implements RoleRequestRepository {
    private final DSLContext dslContext;
    private final static RoleRequestTable roleRequestTable = Tables.ROLE_REQUEST_TABLE;
    private final static RoleTable roleTable = Tables.ROLE_TABLE;
    private final static AccountTable accountTable = Tables.ACCOUNT_TABLE;


    @Override
    public Optional<RoleRequest> findById(UUID id) {
        return dslContext.selectFrom(roleRequestTable)
                .where(roleRequestTable.ID.eq(id))
                .fetchOptional(RoleRequest::fromDAO);
    }

    @Override
    public PaginatedResult<RoleRequest> findAll(String status, UUID ownerId, UUID accountId, int limit, int offset) {
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

        List<RoleRequest> data = dslContext.selectFrom(roleRequestTable).where(condition)
                .orderBy(roleRequestTable.CREATED_AT.desc())
                .limit(limit)
                .offset(offset)
                .fetch(RoleRequest::fromDAO);

        int totalCount = dslContext.fetchCount(roleRequestTable, condition);
        return new PaginatedResult<>(
                data,
                totalCount,
                offset + 1,
                Math.min(offset + limit, totalCount)
        );
    }

    @Override
    public PaginatedResult<RoleRequest> search(String query, int limit, int offset) {
        if (query == null || query.isEmpty()) {
            return findAll(null, null, null, limit, offset);
        }

        Condition condition = roleRequestTable.ROLE_ID.eq(UUID.fromString(query))
                .or(roleTable.NAME.containsIgnoreCase(query))
                .or(accountTable.FIRST_NAME.containsIgnoreCase(query))
                .or(accountTable.LAST_NAME.containsIgnoreCase(query));

        var table = roleRequestTable
                .leftJoin(roleTable).on(roleRequestTable.ROLE_ID.eq(roleTable.ID))
                .leftJoin(accountTable).on(roleRequestTable.ACCOUNT_ID.eq(accountTable.ID));
        Result<Record> records = dslContext.selectFrom(table)
                .where(condition)
                .limit(limit)
                .offset(offset)
                .fetch();

        List<RoleRequest> data = records.map(record -> RoleRequest.fromDAO(record.into(roleRequestTable)));

        int totalCount = dslContext.fetchCount(table, condition);
        return new PaginatedResult<>(
                data,
                totalCount,
                offset + 1,
                Math.min(offset + limit, totalCount)
        );
    }

    @Override
    public void create(RoleRequest roleRequest) {
        dslContext.insertInto(roleRequestTable)
                .set(roleRequestTable.ID, roleRequest.id())
                .set(roleRequestTable.ROLE_ID, roleRequest.roleId())
                .set(roleRequestTable.ACCOUNT_ID, roleRequest.accountId())
                .set(roleRequestTable.OWNER_ID, roleRequest.ownerId())
                .set(roleRequestTable.REASON, roleRequest.reason())
                .set(roleRequestTable.STATUS, roleRequest.status())
                .set(roleRequestTable.CREATED_AT, roleRequest.createdAt())
                .execute();
    }

    @Override
    public void update(RoleRequest roleRequest) {
        dslContext.update(roleRequestTable)
                .set(roleRequestTable.ROLE_ID, roleRequest.roleId())
                .set(roleRequestTable.ACCOUNT_ID, roleRequest.accountId())
                .set(roleRequestTable.OWNER_ID, roleRequest.ownerId())
                .set(roleRequestTable.REASON, roleRequest.reason())
                .set(roleRequestTable.STATUS, roleRequest.status())
                .set(roleRequestTable.RESOLVED_AT, roleRequest.resolvedAt())
                .where(roleRequestTable.ID.eq(roleRequest.id()))
                .execute();
    }

    @Override
    public void setStatus(UUID id, String status) {
        dslContext.update(roleRequestTable)
                .set(roleRequestTable.STATUS, status)
                .where(roleRequestTable.ID.eq(id))
                .execute();
    }
}
