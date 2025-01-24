package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.ResourceDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResourceRepository {
    Optional<ResourceDTO> findById(UUID id);

    List<ResourceDTO> findAll();

    void create(ResourceDTO resource);

    void update(ResourceDTO resource);

    void delete(UUID id);
}