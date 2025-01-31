package com.onyxdb.idm.controllers.v1;

import com.onyxdb.idm.generated.openapi.apis.RolesApi;
import com.onyxdb.idm.generated.openapi.models.ActionPermissionDTO;
import com.onyxdb.idm.generated.openapi.models.ApiPermissionDTO;
import com.onyxdb.idm.generated.openapi.models.RoleDTO;
import com.onyxdb.idm.models.ActionPermission;
import com.onyxdb.idm.models.ApiPermission;
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
    public ResponseEntity<Void> addActionPermissionToRole(UUID roleId, UUID permissionId) {
        roleService.addActionPermission(roleId, permissionId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> removeActionPermissionFromRole(UUID roleId, UUID permissionId) {
        roleService.removeActionPermission(roleId, permissionId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ActionPermissionDTO>> getActionPermissionsByRoleId(UUID roleId) {
        List<ActionPermission> permissions = roleService.getActionPermissionsByRoleId(roleId);
        List<ActionPermissionDTO> permissionDTOs = permissions.stream().map(ActionPermission::toDTO).toList();
        return ResponseEntity.ok(permissionDTOs);
    }

    @Override
    public ResponseEntity<Void> addApiPermissionToRole(UUID roleId, UUID permissionId) {
        roleService.addApiPermission(roleId, permissionId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> removeApiPermissionFromRole(UUID roleId, UUID permissionId) {
        roleService.removeApiPermission(roleId, permissionId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ApiPermissionDTO>> getApiPermissionsByRoleId(UUID roleId) {
        List<ApiPermission> permissions = roleService.getApiPermissionsByRoleId(roleId);
        List<ApiPermissionDTO> permissionDTOs = permissions.stream().map(ApiPermission::toDTO).toList();
        return ResponseEntity.ok(permissionDTOs);
    }
}