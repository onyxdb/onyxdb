package com.onyxdb.platform.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.models.BusinessRole;
import com.onyxdb.platform.idm.models.BusinessRoleWithRoles;
import com.onyxdb.platform.idm.models.OrganizationUnit;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.Permission;
import com.onyxdb.platform.idm.models.Role;
import com.onyxdb.platform.idm.models.RoleWithPermissions;

/**
 * @author ArtemFed
 */
public interface AccountRepository {
    int count();

    Optional<Account> findById(UUID id);

    Optional<Account> findByLogin(String login);

    PaginatedResult<Account> findAll(String query, Integer limit, Integer offset);

    Account create(Account account);

    Account update(Account account);

    void delete(UUID id);

    List<BusinessRole> getAccountBusinessRoles(UUID accountId);

    List<OrganizationUnit> getAccountOrganizationUnits(UUID accountId);

    List<Role> getAccountRoles(UUID accountId);

    List<RoleWithPermissions> getDirectRolesWithPermissions(UUID accountId);

    List<BusinessRoleWithRoles> getBusinessRolesWithRoles(UUID accountId);

    void addBusinessRole(UUID accountId, UUID businessRoleId);

    void removeBusinessRole(UUID accountId, UUID businessRoleId);

    void addRole(UUID accountId, UUID roleId);

    void removeRole(UUID accountId, UUID roleId);

    List<Permission> getPermissions(UUID accountId);
}
