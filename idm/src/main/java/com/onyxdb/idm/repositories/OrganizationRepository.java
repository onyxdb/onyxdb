package com.onyxdb.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.idm.models.Organization;

/**
 * @author ArtemFed
 */
public interface OrganizationRepository {
    Optional<Organization> findById(UUID id);

    List<Organization> findAll();

    void create(Organization organization);

    void update(Organization organization);

    void delete(UUID id);
}
