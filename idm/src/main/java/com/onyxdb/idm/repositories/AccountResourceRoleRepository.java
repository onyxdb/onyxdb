package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.AccountResourceRole;

import java.util.List;
import java.util.UUID;

public interface AccountResourceRoleRepository {
    List<AccountResourceRole> findByAccountId(UUID accountId);

    List<AccountResourceRole> findByResourceId(UUID resourceId);

    void create(AccountResourceRole accountResourceRole);

    void delete(UUID accountId, UUID resourceId, UUID roleId);
}
