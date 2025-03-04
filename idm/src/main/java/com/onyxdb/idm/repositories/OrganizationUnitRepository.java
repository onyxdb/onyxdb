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

    PaginatedResult<OrganizationUnit> findAll(UUID dcId, UUID parentOuId, int limit, int offset);

    void create(OrganizationUnit organizationUnit);

    void update(OrganizationUnit organizationUnit);

    void delete(UUID id);

    List<Account> getOUAccounts(UUID ouId);

    void addAccount(UUID ouId, UUID accountId);

    void removeAccount(UUID ouId, UUID accountId);
}