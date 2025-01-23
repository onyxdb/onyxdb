package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.RoleDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {
    Optional<RoleDTO> findById(UUID id);

    List<RoleDTO> findAll();

    void create(RoleDTO role);

    void update(RoleDTO role);

    void delete(UUID id);
}