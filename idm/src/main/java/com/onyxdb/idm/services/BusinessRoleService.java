package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.BusinessRole;
import com.onyxdb.idm.models.Role;
import com.onyxdb.idm.repositories.BusinessRoleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusinessRoleService {
    private final BusinessRoleRepository businessRoleRepository;

    public BusinessRole findById(UUID id) {
        return businessRoleRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BusinessRole not found"));
    }

    public List<BusinessRole> findAll() {
        return businessRoleRepository.findAll();
    }

    public List<BusinessRole> findByParentId(UUID parentId) {
        return businessRoleRepository.findByParentId(parentId);
    }

    public BusinessRole create(BusinessRole businessRole) {
        BusinessRole forCreate = new BusinessRole(
                UUID.randomUUID(),
                businessRole.name(),
                businessRole.description(),
                businessRole.parentId(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        businessRoleRepository.create(forCreate);
        return forCreate;
    }

    public BusinessRole update(BusinessRole businessRole) {
        BusinessRole forUpdate = new BusinessRole(
                businessRole.id(),
                businessRole.name(),
                businessRole.description(),
                businessRole.parentId(),
                businessRole.createdAt(),
                LocalDateTime.now()
        );
        businessRoleRepository.update(forUpdate);
        return forUpdate;
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
        return businessRoleRepository.getRoleByBusinessRoleId(businessRoleId);
    }
}