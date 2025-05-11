package com.onyxdb.platform.idm.controllers;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.PermissionsApi;
import com.onyxdb.platform.generated.openapi.models.CheckPermission200Response;
import com.onyxdb.platform.generated.openapi.models.PermissionDTO;
import com.onyxdb.platform.idm.common.PermissionCheck;
import com.onyxdb.platform.idm.models.Permission;
import com.onyxdb.platform.idm.services.PermissionService;

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
    @PermissionCheck(entity = "role", action = "get")
    public ResponseEntity<PermissionDTO> getPermissionById(UUID permissionId) {
        Permission permission = permissionService.findById(permissionId);
        return ResponseEntity.ok(permission.toDTO());
    }
}
