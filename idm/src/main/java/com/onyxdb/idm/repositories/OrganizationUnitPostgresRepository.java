package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.AccountOuTable;
import com.onyxdb.idm.generated.jooq.tables.AccountTable;
import com.onyxdb.idm.generated.jooq.tables.OrganizationUnitTable;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.OrganizationUnit;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrganizationUnitPostgresRepository implements OrganizationUnitRepository {

    private final DSLContext dslContext;
    private final OrganizationUnitTable organizationUnitTable = Tables.ORGANIZATION_UNIT_TABLE;
    private final AccountOuTable organizationUnitAccountTable = Tables.ACCOUNT_OU_TABLE;
    private final AccountTable accountTable = Tables.ACCOUNT_TABLE;

    @Override
    public Optional<OrganizationUnit> findById(UUID id) {
        return dslContext.selectFrom(organizationUnitTable)
                .where(organizationUnitTable.ID.eq(id))
                .fetchOptional(record -> new OrganizationUnit(
                        record.getId(),
                        record.getName(),
                        record.getDescription(),
                        record.getDomainComponentId(),
                        record.getParentId(),
                        record.getCreatedAt(),
                        record.getUpdatedAt()
                ));
    }

    @Override
    public List<OrganizationUnit> findAll() {
        return dslContext.selectFrom(organizationUnitTable)
                .fetch(record -> new OrganizationUnit(
                        record.getId(),
                        record.getName(),
                        record.getDescription(),
                        record.getDomainComponentId(),
                        record.getParentId(),
                        record.getCreatedAt(),
                        record.getUpdatedAt()
                ));
    }

    @Override
    public List<OrganizationUnit> findByDomainComponentId(UUID domainComponentId) {
        return dslContext.selectFrom(organizationUnitTable)
                .where(organizationUnitTable.DOMAIN_COMPONENT_ID.eq(domainComponentId))
                .fetch(record -> new OrganizationUnit(
                        record.getId(),
                        record.getName(),
                        record.getDescription(),
                        record.getDomainComponentId(),
                        record.getParentId(),
                        record.getCreatedAt(),
                        record.getUpdatedAt()
                ));
    }

    @Override
    public void create(OrganizationUnit organizationUnit) {
        dslContext.insertInto(organizationUnitTable)
                .set(organizationUnitTable.ID, organizationUnit.id())
                .set(organizationUnitTable.NAME, organizationUnit.name())
                .set(organizationUnitTable.DESCRIPTION, organizationUnit.description())
                .set(organizationUnitTable.DOMAIN_COMPONENT_ID, organizationUnit.domainComponentId())
                .set(organizationUnitTable.PARENT_ID, organizationUnit.parentId())
                .set(organizationUnitTable.CREATED_AT, organizationUnit.createdAt())
                .set(organizationUnitTable.UPDATED_AT, organizationUnit.updatedAt())
                .execute();
    }

    @Override
    public void update(OrganizationUnit organizationUnit) {
        dslContext.update(organizationUnitTable)
                .set(organizationUnitTable.NAME, organizationUnit.name())
                .set(organizationUnitTable.DESCRIPTION, organizationUnit.description())
                .set(organizationUnitTable.DOMAIN_COMPONENT_ID, organizationUnit.domainComponentId())
                .set(organizationUnitTable.PARENT_ID, organizationUnit.parentId())
                .set(organizationUnitTable.UPDATED_AT, organizationUnit.updatedAt())
                .where(organizationUnitTable.ID.eq(organizationUnit.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(organizationUnitTable)
                .where(organizationUnitTable.ID.eq(id))
                .execute();
    }

    /**
     * @param ouId
     * @param accountId
     */
    @Override
    public void addAccount(UUID ouId, UUID accountId) {
        dslContext.insertInto(organizationUnitAccountTable)
                .set(organizationUnitAccountTable.OU_ID, ouId)
                .set(organizationUnitAccountTable.ACCOUNT_ID, accountId)
                .execute();
    }

    /**
     * @param ouId
     * @param accountId
     */
    @Override
    public void removeAccount(UUID ouId, UUID accountId) {
        dslContext.deleteFrom(organizationUnitAccountTable)
                .where(organizationUnitAccountTable.OU_ID.eq(ouId)
                        .and(organizationUnitAccountTable.ACCOUNT_ID.eq(accountId)))
                .execute();
    }

    /**
     * @param ouId
     * @return
     */
    @Override
    public List<Account> getOUAccounts(UUID ouId) {
        return dslContext.selectFrom(organizationUnitAccountTable)
                .where(organizationUnitAccountTable.OU_ID.eq(ouId))
                .fetch(link -> dslContext.selectFrom(accountTable)
                        .where(accountTable.ID.eq(link.getAccountId()))
                        .fetchOne(item -> new Account(
                                item.getId(),
                                item.getUsername(),
                                item.getPassword(),
                                item.getEmail(),
                                item.getFirstName(),
                                item.getLastName(),
                                item.getCreatedAt(),
                                item.getUpdatedAt()
                        ))
                );
    }
}