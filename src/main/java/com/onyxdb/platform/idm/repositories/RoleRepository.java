package com.onyxdb.platform.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.Permission;
import com.onyxdb.platform.idm.models.Role;

/**
 * @author ArtemFed
 */
public interface RoleRepository {
    Optional<Role> findById(UUID id);

    PaginatedResult<Role> findAll(String query, UUID productId, UUID orgUnitId, Integer limit, Integer offset);

    Role create(Role role);

    Role update(Role role);

    void delete(UUID id);

    List<Permission> getPermissions(UUID roleId);

    void addPermission(UUID roleId, UUID permissionId);

    void removePermission(UUID roleId, UUID permissionId);
}
