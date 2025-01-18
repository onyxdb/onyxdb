package com.onyxdb.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.idm.models.Service;

/**
 * @author ArtemFed
 */
public interface ServiceRepository {
    Optional<Service> findById(UUID id);

    List<Service> findByProjectId(UUID projectId);

    void create(Service service);

    void update(Service service);

    void delete(UUID id);
}
