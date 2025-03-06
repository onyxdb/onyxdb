package com.onyxdb.idm.services;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.DomainComponent;
import com.onyxdb.idm.models.OrganizationUnit;
import com.onyxdb.idm.repositories.DomainComponentRepository;
import com.onyxdb.idm.repositories.OrganizationUnitRepository;

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

    public List<DomainComponent> findAll() {
        return domainComponentRepository.findAll();
    }

    public DomainComponent create(DomainComponent domainComponent) {
        return domainComponentRepository.create(domainComponent);
    }

    public DomainComponent update(DomainComponent domainComponent) {
        return domainComponentRepository.update(domainComponent);
    }

    public void delete(UUID id) {
        domainComponentRepository.delete(id);
    }

    public List<OrganizationUnit> findRootOrgUnits(UUID dcId) {
        return organizationUnitRepository.findRootOrgUnits(dcId);
    }
}