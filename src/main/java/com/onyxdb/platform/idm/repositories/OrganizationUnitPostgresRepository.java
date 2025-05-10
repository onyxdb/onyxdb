package com.onyxdb.platform.idm.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.generated.jooq.Tables;
import com.onyxdb.platform.generated.jooq.tables.AccountOuTable;
import com.onyxdb.platform.generated.jooq.tables.AccountTable;
import com.onyxdb.platform.generated.jooq.tables.OrganizationUnitTable;
import com.onyxdb.platform.generated.jooq.tables.records.OrganizationUnitTableRecord;
import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.models.OrganizationTree;
import com.onyxdb.platform.idm.models.OrganizationUnit;
import com.onyxdb.platform.idm.models.PaginatedResult;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.trueCondition;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class OrganizationUnitPostgresRepository implements OrganizationUnitRepository {
    private final static OrganizationUnitTable organizationUnitTable = Tables.ORGANIZATION_UNIT_TABLE;
    private final static AccountOuTable organizationUnitAccountTable = Tables.ACCOUNT_OU_TABLE;
    private final static AccountTable accountTable = Tables.ACCOUNT_TABLE;
    private final DSLContext dslContext;

    @Override
    public Optional<OrganizationUnit> findById(UUID id) {
        return dslContext.selectFrom(organizationUnitTable)
                .where(organizationUnitTable.ID.eq(id))
                .fetchOptional(OrganizationUnit::fromDAO);
    }

    @Override
    public PaginatedResult<OrganizationUnit> findAll(String query, UUID dcId, UUID parentOuId, Integer limit, Integer offset) {
        limit = (limit != null) ? limit : Integer.MAX_VALUE;
        offset = (offset != null) ? offset : 0;

        Condition condition = trueCondition();
        if (dcId != null) {
            condition = condition.and(organizationUnitTable.DOMAIN_COMPONENT_ID.eq(dcId));
        }
        if (parentOuId != null) {
            condition = condition.and(organizationUnitTable.PARENT_ID.eq(parentOuId));
        }
        if (query != null && !query.isEmpty()) {
            condition = condition.and(
                    organizationUnitTable.NAME.containsIgnoreCase(query)
                            .or(organizationUnitTable.DESCRIPTION.containsIgnoreCase(query)));
        }

        List<OrganizationUnit> data = dslContext.selectFrom(organizationUnitTable)
                .where(condition)
                .orderBy(organizationUnitTable.CREATED_AT)
                .fetch(OrganizationUnit::fromDAO);

        int totalCount = dslContext.fetchCount(organizationUnitTable, condition);
        return new PaginatedResult<>(
                data,
                totalCount,
                offset + 1,
                Math.min(offset + limit, totalCount)
        );
    }

    @Override
    public List<OrganizationUnit> findRootOrgUnits(UUID dcId) {
        return dslContext.selectFrom(organizationUnitTable)
                .where(organizationUnitTable.DOMAIN_COMPONENT_ID.eq(dcId)
                        .and(organizationUnitTable.PARENT_ID.isNull()
                                .or(organizationUnitTable.PARENT_ID.eq(organizationUnitTable.ID))))
                .orderBy(organizationUnitTable.CREATED_AT)
                .fetch(OrganizationUnit::fromDAO);
    }

    @Override
    public List<OrganizationTree> findChildrenTree(UUID orgId) {
        List<OrganizationUnit> children = fetchChildrenFromDb(orgId);
        return children.stream()
                .map(child -> new OrganizationTree(child, findChildrenTree(child.id())))
                .collect(Collectors.toList());
    }

    private List<OrganizationUnit> fetchChildrenFromDb(UUID parentId) {
        return dslContext.selectFrom(organizationUnitTable)
                .where(organizationUnitTable.PARENT_ID.eq(parentId))
                .fetchInto(OrganizationUnit.class);
    }

    @Override
    public OrganizationUnit create(OrganizationUnit organizationUnit) {
        var record = dslContext.insertInto(organizationUnitTable)
                .set(organizationUnitTable.ID, UUID.randomUUID())
                .set(organizationUnitTable.NAME, organizationUnit.name())
                .set(organizationUnitTable.DESCRIPTION, organizationUnit.description())
                .set(organizationUnitTable.DOMAIN_COMPONENT_ID, organizationUnit.domainComponentId())
                .set(organizationUnitTable.PARENT_ID, organizationUnit.parentId())
                .set(organizationUnitTable.OWNER_ID, organizationUnit.ownerId())
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
    public List<OrganizationUnit> findAllParentOrganizationUnits(UUID organizationUnitId) {
        var cte = name("recursive_cte").as(
                select(organizationUnitTable.fields())
                        .from(organizationUnitTable)
                        .where(organizationUnitTable.ID.eq(organizationUnitId))
                        .unionAll(
                                select(organizationUnitTable.fields())
                                        .from(organizationUnitTable)
                                        .join(table(name("recursive_cte")))
                                        .on(field(name("recursive_cte", "parent_id"), org.jooq.impl.SQLDataType.UUID)
                                                .eq(organizationUnitTable.ID)))
        );

        return dslContext.withRecursive(cte)
                .selectFrom(cte)
                .fetch()
                .map(record -> OrganizationUnit.fromDAO(record.into(OrganizationUnitTableRecord.class)));
    }

    @Override
    public List<Account> getOUAccounts(UUID ouId) {
        return dslContext.selectFrom(organizationUnitAccountTable)
                .where(organizationUnitAccountTable.OU_ID.eq(ouId))
                .fetch(link -> dslContext.selectFrom(accountTable)
                        .where(accountTable.ID.eq(link.getAccountId()))
                        .orderBy(accountTable.CREATED_AT)
                        .fetchOne(Account::fromDAO)
                );
    }
}