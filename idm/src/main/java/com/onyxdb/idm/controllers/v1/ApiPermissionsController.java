package com.onyxdb.idm.controllers.v1;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.idm.generated.openapi.apis.ApiPermissionsApi;
import com.onyxdb.idm.generated.openapi.models.ApiPermissionDTO;
import com.onyxdb.idm.models.ApiPermission;
import com.onyxdb.idm.services.ApiPermissionService;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class ApiPermissionsController implements ApiPermissionsApi {
    private final ApiPermissionService permissionService;

    @Override
    public ResponseEntity<ApiPermissionDTO> createApiPermission(@Valid ApiPermissionDTO permissionDTO) {
        ApiPermission permission = ApiPermission.fromDTO(permissionDTO);
        ApiPermission createdPermission = permissionService.create(permission);
        return new ResponseEntity<>(createdPermission.toDTO(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteApiPermission(UUID permissionId) {
        permissionService.delete(permissionId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ApiPermissionDTO> getApiPermissionById(UUID permissionId) {
        ApiPermission permission = permissionService.findById(permissionId);
        return ResponseEntity.ok(permission.toDTO());
    }

    @Override
    public ResponseEntity<List<ApiPermissionDTO>> getAllApiPermissions() {
        List<ApiPermission> permissions = permissionService.findAll();
        List<ApiPermissionDTO> permissionDTOs = permissions.stream().map(ApiPermission::toDTO).toList();
        return ResponseEntity.ok(permissionDTOs);
    }

    @Override
    public ResponseEntity<ApiPermissionDTO> updateApiPermission(UUID permissionId, @Valid ApiPermissionDTO permissionDTO) {
        permissionDTO.setId(permissionId);
        ApiPermission permission = ApiPermission.fromDTO(permissionDTO);
        ApiPermission updatedPermission = permissionService.update(permission);
        return ResponseEntity.ok(updatedPermission.toDTO());
    }
}
