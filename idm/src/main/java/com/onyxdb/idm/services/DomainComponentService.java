package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.DomainComponent;
import com.onyxdb.idm.models.OrganizationUnit;
import com.onyxdb.idm.repositories.DomainComponentRepository;
import com.onyxdb.idm.repositories.OrganizationUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author ArtemFed
 */
@Service
@RequiredArgsConstructor
public class DomainComponentService {
    private final DomainComponentRepository domainComponentRepository;
    private final OrganizationUnitRepository organizationUnitRepository;

    public DomainComponent findById(UUID id) {
        return domainComponentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DomainComponent not found"));
    }

    public List<OrganizationUnit> findChildrenOrganizationUnits(UUID id) {
        return organizationUnitRepository.findByDomainComponentId(id);
    }

    public List<DomainComponent> findAll() {
        return domainComponentRepository.findAll();
    }

    public DomainComponent create(DomainComponent domainComponent) {
        DomainComponent forCreate = new DomainComponent(
                UUID.randomUUID(),
                domainComponent.name(),
                domainComponent.description(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        domainComponentRepository.create(forCreate);
        return forCreate;
    }

    public DomainComponent update(DomainComponent domainComponent) {
        DomainComponent forUpdate = new DomainComponent(
                domainComponent.id(),
                domainComponent.name(),
                domainComponent.description(),
                domainComponent.createdAt(),
                LocalDateTime.now()
        );
        domainComponentRepository.update(forUpdate);
        return forUpdate;
    }

    public void delete(UUID id) {
        domainComponentRepository.delete(id);
    }
}