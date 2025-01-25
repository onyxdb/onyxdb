package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.DomainComponent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DomainComponentRepository {
    Optional<DomainComponent> findById(UUID id);

    List<DomainComponent> findAll();

    void create(DomainComponent domainComponent);

    void update(DomainComponent domainComponent);

    void delete(UUID id);
}