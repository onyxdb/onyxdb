package com.onyxdb.idm.repositories;

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

    List<Role> findAll();

    void create(Role role);

    void update(Role role);

    void delete(UUID id);

    void addPermission(UUID roleId, UUID permissionId);

    void removePermission(UUID roleId, UUID permissionId);

    List<Permission> getPermissionsByRoleId(UUID roleId);
}