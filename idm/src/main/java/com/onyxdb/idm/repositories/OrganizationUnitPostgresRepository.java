package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.AccountOuTable;
import com.onyxdb.idm.generated.jooq.tables.AccountTable;
import com.onyxdb.idm.generated.jooq.tables.OrganizationUnitTable;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.OrganizationUnit;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.RoleRequest;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.jooq.impl.DSL.trueCondition;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class OrganizationUnitPostgresRepository implements OrganizationUnitRepository {
    private final DSLContext dslContext;
    private final static OrganizationUnitTable organizationUnitTable = Tables.ORGANIZATION_UNIT_TABLE;
    private final static AccountOuTable organizationUnitAccountTable = Tables.ACCOUNT_OU_TABLE;
    private final static AccountTable accountTable = Tables.ACCOUNT_TABLE;

    @Override
    public Optional<OrganizationUnit> findById(UUID id) {
        return dslContext.selectFrom(organizationUnitTable)
                .where(organizationUnitTable.ID.eq(id))
                .fetchOptional(OrganizationUnit::fromDAO);
    }

    @Override
    public PaginatedResult<OrganizationUnit> findAll(UUID dcId, UUID parentOuId, Integer limit, Integer offset) {
        limit = (limit != null) ? limit : Integer.MAX_VALUE;
        offset = (offset != null) ? offset : 0;

        Condition condition = trueCondition();
        if (dcId != null) {
            condition = condition.and(organizationUnitTable.DOMAIN_COMPONENT_ID.eq(dcId));
        }
        if (parentOuId != null) {
            condition = condition.and(organizationUnitTable.PARENT_ID.eq(parentOuId));
        }

        List<OrganizationUnit> data = dslContext.selectFrom(organizationUnitTable)
                .where(condition).fetch(OrganizationUnit::fromDAO);

        int totalCount = dslContext.fetchCount(organizationUnitTable, condition);

        return new PaginatedResult<>(
                data,
                totalCount,
                offset + 1,
                Math.min(offset + limit, totalCount)
        );
    }

    @Override
    public OrganizationUnit create(OrganizationUnit organizationUnit) {
        var record = dslContext.insertInto(organizationUnitTable)
                .set(organizationUnitTable.ID, UUID.randomUUID())
                .set(organizationUnitTable.NAME, organizationUnit.name())
                .set(organizationUnitTable.DESCRIPTION, organizationUnit.description())
                .set(organizationUnitTable.DOMAIN_COMPONENT_ID, organizationUnit.domainComponentId())
                .set(organizationUnitTable.PARENT_ID, organizationUnit.parentId())
                .set(organizationUnitTable.CREATED_AT, LocalDateTime.now())
                .set(organizationUnitTable.UPDATED_AT, LocalDateTime.now())
                .returning()
                .fetchOne();

        assert record != null;
        return OrganizationUnit.fromDAO(record);
    }

    @Override
    public OrganizationUnit update(OrganizationUnit organizationUnit) {
        var record = dslContext.update(organizationUnitTable)
                .set(organizationUnitTable.NAME, organizationUnit.name())
                .set(organizationUnitTable.DESCRIPTION, organizationUnit.description())
                .set(organizationUnitTable.DOMAIN_COMPONENT_ID, organizationUnit.domainComponentId())
                .set(organizationUnitTable.PARENT_ID, organizationUnit.parentId())
                .set(organizationUnitTable.UPDATED_AT, LocalDateTime.now())
                .where(organizationUnitTable.ID.eq(organizationUnit.id()))
                .returning()
                .fetchOne();

        assert record != null;
        return OrganizationUnit.fromDAO(record);
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(organizationUnitTable)
                .where(organizationUnitTable.ID.eq(id))
                .execute();
    }

    @Override
    public void addAccount(UUID ouId, UUID accountId) {
        dslContext.insertInto(organizationUnitAccountTable)
                .set(organizationUnitAccountTable.OU_ID, ouId)
                .set(organizationUnitAccountTable.ACCOUNT_ID, accountId)
                .execute();
    }

    @Override
    public void removeAccount(UUID ouId, UUID accountId) {
        dslContext.deleteFrom(organizationUnitAccountTable)
                .where(organizationUnitAccountTable.OU_ID.eq(ouId)
                        .and(organizationUnitAccountTable.ACCOUNT_ID.eq(accountId)))
                .execute();
    }

    @Override
    public List<Account> getOUAccounts(UUID ouId) {
        return dslContext.selectFrom(organizationUnitAccountTable)
                .where(organizationUnitAccountTable.OU_ID.eq(ouId))
                .fetch(link -> dslContext.selectFrom(accountTable)
                        .where(accountTable.ID.eq(link.getAccountId()))
                        .fetchOne(Account::fromDAO)
                );
    }
}