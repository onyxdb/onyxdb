package com.onyxdb.platform.idm.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.onyxdb.platform.idm.models.DomainComponent;

/**
 * @author ArtemFed
 */
public interface DomainComponentRepository {
    Optional<DomainComponent> findById(UUID id);

    List<DomainComponent> findAll();

    DomainComponent create(DomainComponent domainComponent);

    DomainComponent update(DomainComponent domainComponent);

    void delete(UUID id);
}
