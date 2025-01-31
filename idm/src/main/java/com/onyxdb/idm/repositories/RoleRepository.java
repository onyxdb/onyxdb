package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.ActionPermission;
import com.onyxdb.idm.models.ApiPermission;
import com.onyxdb.idm.models.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public interface RoleRepository {
    Optional<Role> findById(UUID id);

    List<Role> findAll();

    void create(Role role);

    void update(Role role);

    void delete(UUID id);

    void addActionPermission(UUID roleId, UUID permissionId);

    void removeActionPermission(UUID roleId, UUID permissionId);

    List<ActionPermission> getActionPermissionsByRoleId(UUID roleId);

    void addApiPermission(UUID roleId, UUID permissionId);

    void removeApiPermission(UUID roleId, UUID permissionId);

    List<ApiPermission> getApiPermissionsByRoleId(UUID roleId);
}