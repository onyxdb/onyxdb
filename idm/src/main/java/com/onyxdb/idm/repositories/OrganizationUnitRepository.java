package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.OrganizationUnitDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationUnitRepository {
    Optional<OrganizationUnitDTO> findById(UUID id);

    List<OrganizationUnitDTO> findAll();

    List<OrganizationUnitDTO> findByDomainComponentId(UUID domainComponentId);

    void create(OrganizationUnitDTO organizationUnit);

    void update(OrganizationUnitDTO organizationUnit);

    void delete(UUID id);
}