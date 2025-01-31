package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.BusinessRole;
import com.onyxdb.idm.models.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public interface BusinessRoleRepository {
    Optional<BusinessRole> findById(UUID id);

    List<BusinessRole> findAll();

    List<BusinessRole> findByParentId(UUID parentId);

    void create(BusinessRole businessRole);

    void update(BusinessRole businessRole);

    void delete(UUID id);

    void addRole(UUID businessRoleId, UUID roleId);

    void removeRole(UUID businessRoleId, UUID roleId);

    List<Role> getRoleByBusinessRoleId(UUID businessRoleId);
}
