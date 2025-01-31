package com.onyxdb.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.idm.models.ApiPermission;

/**
 * @author ArtemFed
 */
public interface ApiPermissionRepository {
    Optional<ApiPermission> findById(UUID id);

    List<ApiPermission> findAll();

    void create(ApiPermission permission);

    void update(ApiPermission permission);

    void delete(UUID id);
}