package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.Permission;
import com.onyxdb.idm.models.Role;
import com.onyxdb.idm.repositories.RoleRepository;
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
public class RoleService {
    private final RoleRepository roleRepository;

    public Role findById(UUID id) {
        return roleRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role create(Role role) {
        Role forCreate = new Role(
                UUID.randomUUID(),
                role.name(),
                role.description(),
                role.resourceId(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        roleRepository.create(forCreate);
        return forCreate;
    }

    public Role update(Role role) {
        Role forUpdate = new Role(
                role.id(),
                role.name(),
                role.description(),
                role.resourceId(),
                role.createdAt(),
                LocalDateTime.now()
        );
        roleRepository.update(forUpdate);
        return forUpdate;
    }

    public void delete(UUID id) {
        roleRepository.delete(id);
    }

    public void addPermission(UUID roleId, UUID permissionId) {
        roleRepository.addPermission(roleId, permissionId);
    }

    public void removePermission(UUID roleId, UUID permissionId) {
        roleRepository.removePermission(roleId, permissionId);
    }

    public List<Permission> getPermissionsByRoleId(UUID roleId) {
        return roleRepository.getPermissionsByRoleId(roleId);
    }
}