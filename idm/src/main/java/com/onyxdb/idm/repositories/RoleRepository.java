package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.Permission;
import com.onyxdb.idm.models.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public interface RoleRepository {
    Optional<Role> findById(UUID id);

    PaginatedResult<Role> findAll(String query, UUID productId, UUID orgUnitId, int limit, int offset);

    void create(Role role);

    void update(Role role);

    void delete(UUID id);

    List<Permission> getPermissions(UUID roleId);

    void addPermission(UUID roleId, UUID permissionId);

    void removePermission(UUID roleId, UUID permissionId);
}
