package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Permission;
import com.onyxdb.idm.repositories.PermissionRepository;
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
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public Permission findById(UUID id) {
        return permissionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission create(Permission permission) {
        Permission forCreate = new Permission(
                UUID.randomUUID(),
                permission.actionType(),
                permission.resourceType(),
                permission.resourceFields(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        permissionRepository.create(forCreate);
        return forCreate;
    }

    public Permission update(Permission permission) {
        Permission forUpdate = new Permission(
                permission.id(),
                permission.actionType(),
                permission.resourceType(),
                permission.resourceFields(),
                permission.createdAt(),
                LocalDateTime.now()
        );
        permissionRepository.update(forUpdate);
        return forUpdate;
    }

    public void delete(UUID id) {
        permissionRepository.delete(id);
    }
}