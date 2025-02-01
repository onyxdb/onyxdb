package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.BusinessRole;
import com.onyxdb.idm.models.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public interface AccountRepository {
    Optional<Account> findById(UUID id);

    List<Account> findAll();

    void create(Account account);

    void update(Account account);

    void delete(UUID id);

    void addBusinessRole(UUID accountId, UUID businessRoleId);

    void removeBusinessRole(UUID accountId, UUID businessRoleId);

    List<BusinessRole> getAccountBusinessRoles(UUID accountId);

    void addRole(UUID accountId, UUID roleId);

    void removeRole(UUID accountId, UUID roleId);

    List<Role> getRoleByBusinessRoleId(UUID accountId);
}
