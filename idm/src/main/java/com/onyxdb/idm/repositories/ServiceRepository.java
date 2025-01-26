package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository {
    Optional<Service> findById(UUID id);

    List<Service> findAll();

    List<Service> findByProjectId(UUID projectId);

    void create(Service service);

    void update(Service service);

    void delete(UUID id);
}
