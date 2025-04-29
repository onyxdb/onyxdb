package com.onyxdb.platform.idm.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.onyxdb.platform.idm.v1.ResourceNotFoundException;
import com.onyxdb.platform.idm.models.DomainComponent;
import com.onyxdb.platform.idm.models.DomainTree;
import com.onyxdb.platform.idm.models.OrganizationTree;
import com.onyxdb.platform.idm.models.OrganizationUnit;
import com.onyxdb.platform.idm.repositories.DomainComponentRepository;

/**
 * @author ArtemFed
 */
@Service
@RequiredArgsConstructor
public class DomainComponentService {
    private final DomainComponentRepository domainComponentRepository;
    private final OrganizationUnitService organizationUnitService;

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
        return organizationUnitService.findRootOrgUnits(dcId);
    }

    public DomainTree findDomainTree(UUID dcId) {
        DomainComponent domainComponent = findById(dcId);
        var roots = findRootOrgUnits(dcId);
        List<OrganizationTree> list = new ArrayList<>();
        for (OrganizationUnit root : roots) {
            list.add(organizationUnitService.findChildrenTree(root.id()));
        }
        return new DomainTree(domainComponent, list);
    }
}