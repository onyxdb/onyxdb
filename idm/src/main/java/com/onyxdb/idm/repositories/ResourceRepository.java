package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.Resource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public interface ResourceRepository {
    Optional<Resource> findById(UUID id);

    List<Resource> findAll();

    void create(Resource resource);

    void update(Resource resource);

    void delete(UUID id);
}