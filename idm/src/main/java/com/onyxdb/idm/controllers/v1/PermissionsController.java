package com.onyxdb.idm.controllers.v1;

import com.onyxdb.idm.generated.openapi.apis.PermissionsApi;
import com.onyxdb.idm.generated.openapi.models.PermissionDTO;
import com.onyxdb.idm.models.Permission;
import com.onyxdb.idm.services.PermissionService;
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
public class PermissionsController implements PermissionsApi {
    private final PermissionService permissionService;

    @Override
    public ResponseEntity<PermissionDTO> createPermission(@Valid PermissionDTO permissionDTO) {
        Permission permission = Permission.fromDTO(permissionDTO);
        Permission createdPermission = permissionService.create(permission);
        return new ResponseEntity<>(createdPermission.toDTO(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deletePermission(UUID permissionId) {
        permissionService.delete(permissionId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PermissionDTO> getPermissionById(UUID permissionId) {
        Permission permission = permissionService.findById(permissionId);
        return ResponseEntity.ok(permission.toDTO());
    }

    @Override
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        List<Permission> permissions = permissionService.findAll();
        List<PermissionDTO> permissionDTOs = permissions.stream().map(Permission::toDTO).toList();
        return ResponseEntity.ok(permissionDTOs);
    }

    @Override
    public ResponseEntity<PermissionDTO> updatePermission(UUID permissionId, @Valid PermissionDTO permissionDTO) {
        permissionDTO.setId(permissionId);
        Permission permission = Permission.fromDTO(permissionDTO);
        Permission updatedPermission = permissionService.update(permission);
        return ResponseEntity.ok(updatedPermission.toDTO());
    }
}
