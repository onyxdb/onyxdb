package com.onyxdb.platform.idm.services;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.onyxdb.platform.idm.controllers.ResourceNotFoundException;
import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.models.OrganizationTree;
import com.onyxdb.platform.idm.models.OrganizationUnit;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.repositories.OrganizationUnitRepository;

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

    public PaginatedResult<OrganizationUnit> findAll(String query, UUID dcId, UUID parentOuId, Integer limit, Integer offset) {
        return organizationUnitRepository.findAll(query, dcId, parentOuId, limit, offset);
    }

    public PaginatedResult<OrganizationUnit> findChildren(UUID parentOuId) {
        return organizationUnitRepository.findAll(null, null, parentOuId, 0, 0);
    }

    public List<OrganizationUnit> findRootOrgUnits(UUID dcId) {
        return organizationUnitRepository.findRootOrgUnits(dcId);
    }

    public OrganizationTree findChildrenTree(UUID ouId) {
        var orgUnit = findById(ouId);
        var children = organizationUnitRepository.findChildrenTree(ouId);
        return new OrganizationTree(orgUnit, children);
    }

    public List<OrganizationUnit> findAllParentOrganizationUnits(UUID parentOuId) {
        return organizationUnitRepository.findAllParentOrganizationUnits(parentOuId);
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