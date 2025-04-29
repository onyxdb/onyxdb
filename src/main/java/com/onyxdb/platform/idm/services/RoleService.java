package com.onyxdb.platform.idm.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.onyxdb.platform.idm.controllers.ResourceNotFoundException;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.Permission;
import com.onyxdb.platform.idm.models.Role;
import com.onyxdb.platform.idm.models.RoleWithPermissions;
import com.onyxdb.platform.idm.models.clickhouse.RoleHistory;
import com.onyxdb.platform.idm.repositories.RoleRepository;

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

    public PaginatedResult<Role> findAll(String query, UUID productId, UUID orgUnitId, Integer limit, Integer offset) {
        return roleRepository.findAll(query, productId, orgUnitId, limit, offset);
    }

    public RoleWithPermissions create(RoleWithPermissions roleWithPermission) {
        Role role = roleWithPermission.role();
        Role newRole = roleRepository.create(role);

        List<Permission> permissions = roleWithPermission.permissions();

        List<Permission> newPermissions = permissions.stream().map(permissionService::create).toList();

        newPermissions.forEach(p -> roleRepository.addPermission(newRole.id(), p.id()));

        return new RoleWithPermissions(newRole, newPermissions);
    }

    public RoleWithPermissions update(RoleWithPermissions roleWithPermission) {
        Role role = roleWithPermission.role();
        List<Permission> updatedPermissions = roleWithPermission.permissions();
        Role newRole = roleRepository.update(role);

        List<Permission> existingPermissions = roleRepository.getPermissions(role.id());
        Map<UUID, Permission> updatedPermIds = new HashMap<>();
        updatedPermissions.forEach(p -> {
            if (p.id() != null) {
                updatedPermIds.put(p.id(), p);
            }
        });

        Map<UUID, Permission> existingPermIds = new HashMap<>();
        existingPermissions.forEach(p -> existingPermIds.put(p.id(), p));

        List<Permission> newPermissions = new java.util.ArrayList<>(List.of());
        for (Permission p : existingPermissions) {
            if (updatedPermIds.containsKey(p.id())) {
                Permission updatedPermission = permissionService.update(p);
                newPermissions.add(updatedPermission);
            } else {
                roleRepository.removePermission(role.id(), p.id());
            }
        }

        for (Permission p : updatedPermissions) {
            if (p.id() == null || !existingPermIds.containsKey(p.id())) {
                var newPermission = permissionService.create(p);
                newPermissions.add(newPermission);
                roleRepository.addPermission(role.id(), newPermission.id());
            }
        }

        return new RoleWithPermissions(newRole, newPermissions);
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

    public RoleWithPermissions getPermissionsByRoleId(UUID roleId) {
        Role role = findById(roleId);
        List<Permission> permissions = roleRepository.getPermissions(roleId);
        return new RoleWithPermissions(role, permissions);
    }

    public RoleHistory getRoleHistory(UUID roleId) {
        return null;
    }
}