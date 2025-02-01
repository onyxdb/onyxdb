package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.Permission;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public interface PermissionRepository {
    Optional<Permission> findById(UUID id);

    List<Permission> findAll();

    void create(Permission permission);

    void update(Permission permission);

    void delete(UUID id);
}