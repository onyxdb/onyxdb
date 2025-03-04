package com.onyxdb.idm.controllers.v1;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.idm.generated.openapi.apis.RolesApi;
import com.onyxdb.idm.generated.openapi.models.PaginatedRoleResponse;
import com.onyxdb.idm.generated.openapi.models.PermissionDTO;
import com.onyxdb.idm.generated.openapi.models.RoleDTO;
import com.onyxdb.idm.generated.openapi.models.RoleWithPermissionsDTO;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.Permission;
import com.onyxdb.idm.models.Role;
import com.onyxdb.idm.models.RoleWithPermissions;
import com.onyxdb.idm.services.RoleService;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class RolesController implements RolesApi {
    private final RoleService roleService;

    @Override
    public ResponseEntity<RoleDTO> getRoleById(UUID roleId) {
        Role role = roleService.findById(roleId);
        return ResponseEntity.ok(role.toDTO());
    }

    @Override
    public ResponseEntity<PaginatedRoleResponse> getAllRoles(
            String search, UUID productId, UUID orgUnitId, Integer limit, Integer offset
    ) {
        PaginatedResult<Role> roles = roleService.findAll(search, productId, orgUnitId, limit, offset);
        List<RoleDTO> roleDTOs = roles.data().stream().map(Role::toDTO).toList();
        return ResponseEntity.ok(new PaginatedRoleResponse()
                .data(roleDTOs)
                .totalCount(roles.totalCount())
                .startPosition(roles.startPosition())
                .endPosition(roles.endPosition())
        );
    }

    @Override
    public ResponseEntity<List<PermissionDTO>> getPermissionsByRoleId(UUID roleId) {
        List<Permission> permissions = roleService.getPermissionsByRoleId(roleId);
        List<PermissionDTO> permissionDTOS = permissions.stream().map(Permission::toDTO).toList();
        return ResponseEntity.ok(permissionDTOS);
    }

    @Override
    public ResponseEntity<RoleWithPermissionsDTO> createRole(@Valid RoleWithPermissionsDTO roleDTO) {
        RoleWithPermissions createdRole = roleService.create(RoleWithPermissions.fromDTO(roleDTO));
        return new ResponseEntity<>(createdRole.toDTO(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<RoleWithPermissionsDTO> updateRole(UUID roleId, @Valid RoleWithPermissionsDTO roleDTO) {
        RoleWithPermissions updatedRole = roleService.update(RoleWithPermissions.fromDTO(roleDTO));
        return ResponseEntity.ok(updatedRole.toDTO());
    }

    @Override
    public ResponseEntity<Void> deleteRole(UUID roleId) {
        roleService.delete(roleId);
        return ResponseEntity.noContent().build();
    }
}
