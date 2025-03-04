package com.onyxdb.idm.services;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.Permission;
import com.onyxdb.idm.models.Role;
import com.onyxdb.idm.models.RoleWithPermissions;
import com.onyxdb.idm.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author ArtemFed
 */
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    public Role findById(UUID id) {
        return roleRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    public PaginatedResult<Role> findAll(String query, UUID productId, UUID orgUnitId, int limit, int offset) {
        return roleRepository.findAll(query, productId, orgUnitId, limit, offset);
    }

    public RoleWithPermissions create(RoleWithPermissions roleWithPermission) {
        Role role = roleWithPermission.role();
        Role newRole = roleRepository.create(role);

        List<Permission> permissions = roleWithPermission.permissions();

        List<Permission> newPermissions = permissions.stream().map(permissionService::create).toList();

        newPermissions.forEach(p -> roleRepository.addPermission(newRole.id(), p.id()));

        return new RoleWithPermissions(role, newPermissions);
    }

    public RoleWithPermissions update(RoleWithPermissions roleWithPermission) {
        Role role = roleWithPermission.role();
        List<Permission> updatedPermissions = roleWithPermission.permissions();
        roleRepository.update(role);

        List<Permission> existingPermissions = roleRepository.getPermissions(role.id());
        Map<UUID, Permission> updatedPermIds = new HashMap<>();
        updatedPermissions.forEach(p -> updatedPermIds.put(p.id(), p));

        Map<UUID, Permission> existingPermIds = new HashMap<>();
        existingPermissions.forEach(p -> existingPermIds.put(p.id(), p));

        for (Permission p : existingPermissions) {
            if (updatedPermIds.containsKey(p.id())) {
                permissionService.update(p);
            } else {
                roleRepository.removePermission(role.id(), p.id());
            }
        }

        List<Permission> newPermissions = new java.util.ArrayList<>(List.of());
        for (Permission p : updatedPermissions) {
            if (p.id() != null && !existingPermIds.containsKey(p.id())) {
                var newPermission = permissionService.create(p);
                newPermissions.add(newPermission);
                roleRepository.addPermission(role.id(), newPermission.id());
            }
        }

        return new RoleWithPermissions(role, newPermissions);
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
        return roleRepository.getPermissions(roleId);
    }
}