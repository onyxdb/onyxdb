package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.OrganizationDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository {
    Optional<OrganizationDTO> findById(UUID id);

    List<OrganizationDTO> findAll();

    void create(OrganizationDTO organization);

    void update(OrganizationDTO organization);

    void delete(UUID id);
}