package com.onyxdb.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.idm.models.ServiceDTO;

/**
 * @author ArtemFed
 */
public interface ServiceRepository {
    Optional<ServiceDTO> findById(UUID id);

    List<ServiceDTO> findByProjectId(UUID projectId);

    void create(ServiceDTO service);

    void update(ServiceDTO service);

    void delete(UUID id);
}
