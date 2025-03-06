package com.onyxdb.idm.services;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Permission;
import com.onyxdb.idm.repositories.PermissionRepository;

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
        return permissionRepository.create(permission);
    }

    public Permission update(Permission permission) {
        return permissionRepository.update(permission);
    }

    public void delete(UUID id) {
        permissionRepository.delete(id);
    }
}