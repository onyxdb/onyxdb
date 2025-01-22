package com.onyxdb.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.idm.models.GroupDTO;

/**
 * @author ArtemFed
 */
public interface GroupRepository {
    Optional<GroupDTO> findById(UUID id);

    List<GroupDTO> findAll();

    void create(GroupDTO group);

    void update(GroupDTO group);

    void delete(UUID id);
}
