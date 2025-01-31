package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.ActionPermission;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public interface ActionPermissionRepository {
    Optional<ActionPermission> findById(UUID id);

    List<ActionPermission> findAll();

    void create(ActionPermission permission);

    void update(ActionPermission permission);

    void delete(UUID id);
}