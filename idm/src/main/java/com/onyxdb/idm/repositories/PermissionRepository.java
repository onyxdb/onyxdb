package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.PermissionDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository {
    Optional<PermissionDTO> findById(UUID id);

    List<PermissionDTO> findAll();

    void create(PermissionDTO permission);

    void update(PermissionDTO permission);

    void delete(UUID id);
}