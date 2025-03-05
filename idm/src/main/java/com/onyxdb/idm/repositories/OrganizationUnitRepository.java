package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.OrganizationUnit;
import com.onyxdb.idm.models.PaginatedResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public interface OrganizationUnitRepository {
    Optional<OrganizationUnit> findById(UUID id);

    PaginatedResult<OrganizationUnit> findAll(UUID dcId, UUID parentOuId, Integer limit, Integer offset);

    OrganizationUnit create(OrganizationUnit organizationUnit);

    OrganizationUnit update(OrganizationUnit organizationUnit);

    void delete(UUID id);

    List<Account> getOUAccounts(UUID ouId);

    void addAccount(UUID ouId, UUID accountId);

    void removeAccount(UUID ouId, UUID accountId);
}