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
}
