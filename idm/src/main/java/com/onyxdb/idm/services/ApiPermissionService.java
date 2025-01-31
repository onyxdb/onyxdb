package com.onyxdb.idm.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.ApiPermission;
import com.onyxdb.idm.repositories.ActionPermissionRepository;
import com.onyxdb.idm.repositories.ApiPermissionRepository;

/**
 * @author ArtemFed
 */
@Service
@RequiredArgsConstructor
public class ApiPermissionService {
    private final ApiPermissionRepository permissionRepository;

    public ApiPermission findById(UUID id) {
        return permissionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
    }

    public List<ApiPermission> findAll() {
        return permissionRepository.findAll();
    }

    public ApiPermission create(ApiPermission permission) {
        ApiPermission forCreate = new ApiPermission(
                UUID.randomUUID(),
                permission.apiPathRegexp(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        permissionRepository.create(forCreate);
        return forCreate;
    }

    public ApiPermission update(ApiPermission permission) {
        ApiPermission forUpdate = new ApiPermission(
                permission.id(),
                permission.apiPathRegexp(),
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