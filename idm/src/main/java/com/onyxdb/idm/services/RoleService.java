package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.ActionPermission;
import com.onyxdb.idm.models.ApiPermission;
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
                role.role_type(),
                role.name(),
                role.shop_name(),
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
                role.role_type(),
                role.name(),
                role.shop_name(),
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

    public void addActionPermission(UUID roleId, UUID permissionId) {
        roleRepository.addActionPermission(roleId, permissionId);
    }

    public void removeActionPermission(UUID roleId, UUID permissionId) {
        roleRepository.removeActionPermission(roleId, permissionId);
    }

    public List<ActionPermission> getActionPermissionsByRoleId(UUID roleId) {
        return roleRepository.getActionPermissionsByRoleId(roleId);
    }

    public void addApiPermission(UUID roleId, UUID permissionId) {
        roleRepository.addApiPermission(roleId, permissionId);
    }

    public void removeApiPermission(UUID roleId, UUID permissionId) {
        roleRepository.removeApiPermission(roleId, permissionId);
    }

    public List<ApiPermission> getApiPermissionsByRoleId(UUID roleId) {
        return roleRepository.getApiPermissionsByRoleId(roleId);
    }
}