package com.onyxdb.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.idm.models.Group;

/**
 * @author ArtemFed
 */
public interface GroupRepository {
    Optional<Group> findById(UUID id);

    List<Group> findAll();

    void create(Group group);

    void update(Group group);

    void delete(UUID id);
}
