package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.BusinessRole;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public interface BusinessRoleRepository {
    Optional<BusinessRole> findById(UUID id);

//    List<BusinessRole> findAll();

    PaginatedResult<BusinessRole> findAll(String query, Integer limit, Integer offset);

    List<BusinessRole> findChildren(UUID parentId);

    List<BusinessRole> findParents(UUID id);

    BusinessRole create(BusinessRole businessRole);

    BusinessRole update(BusinessRole businessRole);

    void delete(UUID id);

    List<Role> getRoles(UUID businessRoleId);

    void addRole(UUID businessRoleId, UUID roleId);

    void removeRole(UUID businessRoleId, UUID roleId);
}
