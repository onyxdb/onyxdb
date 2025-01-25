package com.onyxdb.idm.services;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.DomainComponentTable;
import com.onyxdb.idm.models.DomainComponent;
import com.onyxdb.idm.repositories.DomainComponentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DomainComponentService {

    private final DomainComponentRepository domainComponentRepository;
    private final DomainComponentTable domainComponentTable = Tables.DOMAIN_COMPONENT_TABLE;

    public Optional<DomainComponent> findById(UUID id) {
        return domainComponentRepository.findById(id);
    }

    public List<DomainComponent> findAll() {
        return domainComponentRepository.findAll();
    }

    @Transactional
    public void create(DomainComponent domainComponent) {
        domainComponentRepository.create(domainComponent);
    }

    @Transactional
    public void update(DomainComponent domainComponent) {
        domainComponentRepository.update(domainComponent);
    }

    @Transactional
    public void delete(UUID id) {
        domainComponentRepository.delete(id);
    }
}