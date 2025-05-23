package com.onyxdb.platform.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.models.OrganizationTree;
import com.onyxdb.platform.idm.models.OrganizationUnit;
import com.onyxdb.platform.idm.models.PaginatedResult;

/**
 * @author ArtemFed
 */
public interface OrganizationUnitRepository {
    Optional<OrganizationUnit> findById(UUID id);

    PaginatedResult<OrganizationUnit> findAll(String query, UUID dcId, UUID parentOuId, Integer limit, Integer offset);

    List<OrganizationUnit> findRootOrgUnits(UUID dcId);

    List<OrganizationTree> findChildrenTree(UUID orgId);

    OrganizationUnit create(OrganizationUnit organizationUnit);

    OrganizationUnit update(OrganizationUnit organizationUnit);

    void delete(UUID id);

    List<Account> getOUAccounts(UUID ouId);

    void addAccount(UUID ouId, UUID accountId);

    void removeAccount(UUID ouId, UUID accountId);

    List<OrganizationUnit> findAllParentOrganizationUnits(UUID organizationUnitId);
}