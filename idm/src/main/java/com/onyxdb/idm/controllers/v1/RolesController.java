package com.onyxdb.idm.controllers.v1;

import com.onyxdb.idm.generated.openapi.apis.RolesApi;
import com.onyxdb.idm.generated.openapi.models.PermissionDTO;
import com.onyxdb.idm.generated.openapi.models.RoleDTO;
import com.onyxdb.idm.models.Permission;
import com.onyxdb.idm.models.Role;
import com.onyxdb.idm.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class RolesController implements RolesApi {
    private final RoleService roleService;

    @Override
    public ResponseEntity<RoleDTO> createRole(@Valid RoleDTO roleDTO) {
        Role role = Role.fromDTO(roleDTO);
        Role createdRole = roleService.create(role);
        return new ResponseEntity<>(createdRole.toDTO(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteRole(UUID roleId) {
        roleService.delete(roleId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RoleDTO> getRoleById(UUID roleId) {
        Role role = roleService.findById(roleId);
        return ResponseEntity.ok(role.toDTO());
    }

    @Override
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<Role> roles = roleService.findAll();
        List<RoleDTO> roleDTOs = roles.stream().map(Role::toDTO).toList();
        return ResponseEntity.ok(roleDTOs);
    }

    @Override
    public ResponseEntity<RoleDTO> updateRole(UUID roleId, @Valid RoleDTO roleDTO) {
        roleDTO.setId(roleId);
        Role role = Role.fromDTO(roleDTO);
        Role updatedRole = roleService.update(role);
        return ResponseEntity.ok(updatedRole.toDTO());
    }

    @Override
    public ResponseEntity<Void> addPermissionToRole(UUID roleId, UUID permissionId) {
        roleService.addPermission(roleId, permissionId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> removePermissionFromRole(UUID roleId, UUID permissionId) {
        roleService.removePermission(roleId, permissionId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<PermissionDTO>> getPermissionsByRoleId(UUID roleId) {
        List<Permission> permissions = roleService.getPermissionsByRoleId(roleId);
        List<PermissionDTO> permissionDTOs = permissions.stream().map(Permission::toDTO).toList();
        return ResponseEntity.ok(permissionDTOs);
    }
}