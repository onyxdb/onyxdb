package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.AccountResourceRoleDTO;

import java.util.List;
import java.util.UUID;

public interface AccountResourceRoleRepository {
    List<AccountResourceRoleDTO> findByAccountId(UUID accountId);

    List<AccountResourceRoleDTO> findByResourceId(UUID resourceId);

    void create(AccountResourceRoleDTO accountResourceRole);

    void delete(UUID accountId, UUID resourceId, UUID roleId);
}
