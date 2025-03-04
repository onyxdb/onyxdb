package com.onyxdb.idm.controllers.v1;

import com.onyxdb.idm.generated.openapi.apis.PermissionsApi;
import com.onyxdb.idm.generated.openapi.models.CheckPermission200Response;
import com.onyxdb.idm.generated.openapi.models.PermissionDTO;
import com.onyxdb.idm.models.Permission;
import com.onyxdb.idm.services.PermissionService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class PermissionsController implements PermissionsApi {
    private final PermissionService permissionService;

    @Override
    public ResponseEntity<CheckPermission200Response> checkPermission(String actionType, UUID resourceId) {
        // TODO
        return null;
    }

    @Override
    public ResponseEntity<PermissionDTO> getPermissionById(UUID permissionId) {
        Permission permission = permissionService.findById(permissionId);
        return ResponseEntity.ok(permission.toDTO());
    }
}
