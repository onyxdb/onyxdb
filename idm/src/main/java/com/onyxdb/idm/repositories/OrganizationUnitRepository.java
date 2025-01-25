package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.OrganizationUnit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationUnitRepository {
    Optional<OrganizationUnit> findById(UUID id);

    List<OrganizationUnit> findAll();

    List<OrganizationUnit> findByDomainComponentId(UUID domainComponentId);

    void create(OrganizationUnit organizationUnit);

    void update(OrganizationUnit organizationUnit);

    void delete(UUID id);
}