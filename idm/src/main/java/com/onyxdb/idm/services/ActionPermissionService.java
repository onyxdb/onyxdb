package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.ActionPermission;
import com.onyxdb.idm.repositories.ActionPermissionRepository;
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
public class ActionPermissionService {
    private final ActionPermissionRepository permissionRepository;

    public ActionPermission findById(UUID id) {
        return permissionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
    }

    public List<ActionPermission> findAll() {
        return permissionRepository.findAll();
    }

    public ActionPermission create(ActionPermission permission) {
        ActionPermission forCreate = new ActionPermission(
                UUID.randomUUID(),
                permission.actionType(),
                permission.resourceFields(),
                permission.labels(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        permissionRepository.create(forCreate);
        return forCreate;
    }

    public ActionPermission update(ActionPermission permission) {
        ActionPermission forUpdate = new ActionPermission(
                permission.id(),
                permission.actionType(),
                permission.resourceFields(),
                permission.labels(),
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