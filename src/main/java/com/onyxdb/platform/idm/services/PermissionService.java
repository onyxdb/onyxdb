package com.onyxdb.platform.idm.services;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.onyxdb.platform.idm.models.exceptions.ResourceNotFoundException;
import com.onyxdb.platform.idm.models.Permission;
import com.onyxdb.platform.idm.repositories.PermissionRepository;

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