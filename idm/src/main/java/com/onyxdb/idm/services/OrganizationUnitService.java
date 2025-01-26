package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.OrganizationUnit;
import com.onyxdb.idm.repositories.OrganizationUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationUnitService {
    private final OrganizationUnitRepository organizationUnitRepository;

    public OrganizationUnit findById(UUID id) {
        return organizationUnitRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrganizationUnit not found"));
    }

    public List<OrganizationUnit> findAll() {
        return organizationUnitRepository.findAll();
    }

    public List<OrganizationUnit> findByDomainComponentId(UUID domainComponentId) {
        return organizationUnitRepository.findByDomainComponentId(domainComponentId);
    }

    public OrganizationUnit create(OrganizationUnit organizationUnit) {
        OrganizationUnit forCreate = new OrganizationUnit(
                UUID.randomUUID(),
                organizationUnit.name(),
                organizationUnit.description(),
                organizationUnit.domainComponentId(),
                organizationUnit.parentId(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        organizationUnitRepository.create(forCreate);
        return forCreate;
    }

    public OrganizationUnit update(OrganizationUnit organizationUnit) {
        OrganizationUnit forUpdate = new OrganizationUnit(
                organizationUnit.id(),
                organizationUnit.name(),
                organizationUnit.description(),
                organizationUnit.domainComponentId(),
                organizationUnit.parentId(),
                organizationUnit.createdAt(),
                LocalDateTime.now()
        );
        organizationUnitRepository.update(forUpdate);
        return forUpdate;
    }

    public void delete(UUID id) {
        organizationUnitRepository.delete(id);
    }

    public void addAccount(UUID ouId, UUID accountId) {
        organizationUnitRepository.addAccount(ouId, accountId);
    }

    public void removeAccount(UUID ouId, UUID accountId) {
        organizationUnitRepository.removeAccount(ouId, accountId);
    }

    public List<Account> getOUAccounts(UUID ouId) {
        return organizationUnitRepository.getOUAccounts(ouId);
    }
}