package com.onyxdb.platform.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.platform.idm.models.BusinessRole;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.Role;

/**
 * @author ArtemFed
 */
public interface BusinessRoleRepository {
    Optional<BusinessRole> findById(UUID id);

//    List<BusinessRole> findAll();

    PaginatedResult<BusinessRole> findAll(String query, Integer limit, Integer offset);

    List<BusinessRole> findChildren(UUID parentId);

    List<BusinessRole> findAllParents(UUID id);

    List<BusinessRole> findBusinessRolesWithHierarchyByAccountId(UUID accountId);

    BusinessRole create(BusinessRole businessRole);

    BusinessRole update(BusinessRole businessRole);

    void delete(UUID id);

    List<Role> getRoles(UUID businessRoleId);

    void addRole(UUID businessRoleId, UUID roleId);

    void removeRole(UUID businessRoleId, UUID roleId);
}
