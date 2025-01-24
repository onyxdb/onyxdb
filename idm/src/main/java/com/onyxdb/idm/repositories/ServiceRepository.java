package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.ServiceDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository {
    Optional<ServiceDTO> findById(UUID id);

    List<ServiceDTO> findAll();

    List<ServiceDTO> findByProjectId(UUID projectId);

    void create(ServiceDTO service);

    void update(ServiceDTO service);

    void delete(UUID id);
}