package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.DomainComponent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
