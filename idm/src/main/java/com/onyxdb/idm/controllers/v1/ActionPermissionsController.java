package com.onyxdb.idm.controllers.v1;

import com.onyxdb.idm.generated.openapi.apis.ActionPermissionsApi;
import com.onyxdb.idm.generated.openapi.models.ActionPermissionDTO;
import com.onyxdb.idm.models.ActionPermission;
import com.onyxdb.idm.services.ActionPermissionService;
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
public class ActionPermissionsController implements ActionPermissionsApi {
    private final ActionPermissionService permissionService;

    @Override
    public ResponseEntity<ActionPermissionDTO> createActionPermission(@Valid ActionPermissionDTO ActionPermissionDTO) {
        ActionPermission permission = ActionPermission.fromDTO(ActionPermissionDTO);
        ActionPermission createdPermission = permissionService.create(permission);
        return new ResponseEntity<>(createdPermission.toDTO(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteActionPermission(UUID permissionId) {
        permissionService.delete(permissionId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ActionPermissionDTO> getActionPermissionById(UUID permissionId) {
        ActionPermission permission = permissionService.findById(permissionId);
        return ResponseEntity.ok(permission.toDTO());
    }

    @Override
    public ResponseEntity<List<ActionPermissionDTO>> getAllActionsPermissions() {
        List<ActionPermission> permissions = permissionService.findAll();
        List<ActionPermissionDTO> ActionPermissionDTOs = permissions.stream().map(ActionPermission::toDTO).toList();
        return ResponseEntity.ok(ActionPermissionDTOs);
    }

    @Override
    public ResponseEntity<ActionPermissionDTO> updateActionPermission(UUID permissionId, @Valid ActionPermissionDTO ActionPermissionDTO) {
        ActionPermissionDTO.setId(permissionId);
        ActionPermission permission = ActionPermission.fromDTO(ActionPermissionDTO);
        ActionPermission updatedPermission = permissionService.update(permission);
        return ResponseEntity.ok(updatedPermission.toDTO());
    }
}
