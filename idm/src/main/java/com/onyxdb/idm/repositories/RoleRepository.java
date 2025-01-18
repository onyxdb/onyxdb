package com.onyxdb.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.idm.models.Role;

/**
 * @author ArtemFed
 */
public interface RoleRepository {
    Optional<Role> findById(UUID id);

    List<Role> findAll();

    void create(Role role);

    void update(Role role);

    void delete(UUID id);
}
