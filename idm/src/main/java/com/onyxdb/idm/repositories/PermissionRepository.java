package com.onyxdb.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.idm.models.Permission;

/**
 * @author ArtemFed
 */
public interface PermissionRepository {
    Optional<Permission> findById(UUID id);

    List<Permission> findAll();

    Permission create(Permission permission);

    Permission update(Permission permission);

    void delete(UUID id);

    List<Permission> findAccountPermissionsToProduct(UUID accountId, UUID productId, String permissionType);

    List<Permission> findAccountPermissionsToOrgUnit(UUID accountId, UUID orgUnitId, String permissionType);

    List<Permission> findAllAccountPermissions(UUID accountId, String resourceType);

    List<Permission> findAccountPermissionsViaBusinessRoles(UUID accountId, String resourceType, String permissionType);
}
