package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.BusinessRole;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.Permission;
import com.onyxdb.idm.models.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public interface AccountRepository {
    Optional<Account> findById(UUID id);

    Optional<Account> findByLogin(String login);

    PaginatedResult<Account> findAll(String query, int limit, int offset);

    Account create(Account account);

    Account update(Account account);

    void delete(UUID id);

    List<BusinessRole> getAccountBusinessRoles(UUID accountId);

    List<Role> getAccountRoles(UUID accountId);

    void addBusinessRole(UUID accountId, UUID businessRoleId);

    void removeBusinessRole(UUID accountId, UUID businessRoleId);

    List<Role> getRoles(UUID accountId);

    void addRole(UUID accountId, UUID roleId);

    void removeRole(UUID accountId, UUID roleId);

    List<Permission> getPermissions(UUID accountId);
}
