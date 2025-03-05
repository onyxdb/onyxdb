package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.BusinessRole;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.Role;
import com.onyxdb.idm.repositories.BusinessRoleRepository;

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
public class BusinessRoleService {
    private final BusinessRoleRepository businessRoleRepository;

    public BusinessRole findById(UUID id) {
        return businessRoleRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BusinessRole not found"));
    }

    public PaginatedResult<BusinessRole> findAll(String search, Integer limit, Integer offset) {
        return businessRoleRepository.findAll(search, limit, offset);
    }

    public List<BusinessRole> findChildren(UUID parentId) {
        return businessRoleRepository.findChildren(parentId);
    }

    public List<BusinessRole> findParents(UUID id) {
        return businessRoleRepository.findParents(id);
    }

    public BusinessRole create(BusinessRole businessRole) {
        return businessRoleRepository.create(businessRole);
    }

    public BusinessRole update(BusinessRole businessRole) {
        return businessRoleRepository.update(businessRole);
    }

    public void delete(UUID id) {
        businessRoleRepository.delete(id);
    }

    public void addRole(UUID businessRoleId, UUID roleId) {
        businessRoleRepository.addRole(businessRoleId, roleId);
    }

    public void removeRole(UUID businessRoleId, UUID roleId) {
        businessRoleRepository.removeRole(businessRoleId, roleId);
    }

    public List<Role> getRolesByBusinessRoleId(UUID businessRoleId) {
        return businessRoleRepository.getRoles(businessRoleId);
    }
}