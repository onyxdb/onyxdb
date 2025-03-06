package com.onyxdb.idm.services;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.OrganizationUnit;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.repositories.OrganizationUnitRepository;

/**
 * @author ArtemFed
 */
@Service
@RequiredArgsConstructor
public class OrganizationUnitService {
    private final OrganizationUnitRepository organizationUnitRepository;

    public OrganizationUnit findById(UUID id) {
        return organizationUnitRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrganizationUnit not found"));
    }

    public PaginatedResult<OrganizationUnit> findAll(UUID dcId, UUID parentOuId, Integer limit, Integer offset) {
        return organizationUnitRepository.findAll(dcId, parentOuId, limit, offset);
    }

    public PaginatedResult<OrganizationUnit> findChildren(UUID parentOuId) {
        return organizationUnitRepository.findAll(null, parentOuId, 0, 0);
    }

    public OrganizationUnit create(OrganizationUnit organizationUnit) {
        return organizationUnitRepository.create(organizationUnit);
    }

    public OrganizationUnit update(OrganizationUnit organizationUnit) {
        return organizationUnitRepository.update(organizationUnit);
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