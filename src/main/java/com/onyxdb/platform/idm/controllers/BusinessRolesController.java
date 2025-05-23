package com.onyxdb.platform.idm.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.BusinessRolesApi;
import com.onyxdb.platform.generated.openapi.models.BusinessRolePostDTO;
import com.onyxdb.platform.generated.openapi.models.BusinessRoleDTO;
import com.onyxdb.platform.generated.openapi.models.PaginatedBusinessRoleResponse;
import com.onyxdb.platform.generated.openapi.models.RoleDTO;
import com.onyxdb.platform.idm.common.PermissionCheck;
import com.onyxdb.platform.idm.models.BusinessRole;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.Role;
import com.onyxdb.platform.idm.services.BusinessRoleService;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class BusinessRolesController implements BusinessRolesApi {
    private final BusinessRoleService businessRoleService;

    @Override
    @PermissionCheck(entity = "business-role", action = "create")
    public ResponseEntity<BusinessRoleDTO> createBusinessRole(@Valid BusinessRolePostDTO businessRoleDTO) {
        BusinessRole businessRole = BusinessRole.fromPostDTO(businessRoleDTO);
        BusinessRole createdBusinessRole = businessRoleService.create(businessRole);
        return new ResponseEntity<>(createdBusinessRole.toDTO(), HttpStatus.CREATED);
    }

    @Override
    @PermissionCheck(entity = "business-role", action = "delete")
    public ResponseEntity<Void> deleteBusinessRole(UUID businessRoleId) {
        businessRoleService.delete(businessRoleId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PermissionCheck(entity = "business-role", action = "get")
    public ResponseEntity<PaginatedBusinessRoleResponse> getAllBusinessRoles(
            String search, Integer limit, Integer offset
    ) {
        PaginatedResult<BusinessRole> businessRoles = businessRoleService.findAll(search, limit, offset);
        List<BusinessRoleDTO> businessRoleDTOs = businessRoles.data().stream().map(BusinessRole::toDTO).toList();
        return ResponseEntity.ok(new PaginatedBusinessRoleResponse()
                .data(businessRoleDTOs)
                .totalCount(businessRoles.totalCount())
                .startPosition(businessRoles.startPosition())
                .endPosition(businessRoles.endPosition())
        );
    }

    @Override
    @PermissionCheck(entity = "business-role", action = "get")
    public ResponseEntity<BusinessRoleDTO> getBusinessRoleById(UUID businessRoleId) {
        BusinessRole businessRole = businessRoleService.findById(businessRoleId);
        return ResponseEntity.ok(businessRole.toDTO());
    }

    @Override
    @PermissionCheck(entity = "business-role", action = "get")
    public ResponseEntity<List<BusinessRoleDTO>> getBusinessRoleChildrenBRs(UUID businessRoleId) {
        List<BusinessRole> businessRoles = businessRoleService.findChildren(businessRoleId);
        List<BusinessRoleDTO> businessRoleDTOs = businessRoles.stream().map(BusinessRole::toDTO).toList();
        return ResponseEntity.ok(businessRoleDTOs);
    }

    @Override
    @PermissionCheck(entity = "business-role", action = "get")
    public ResponseEntity<List<BusinessRoleDTO>> getBusinessRoleParentsBRs(UUID businessRoleId) {
        List<BusinessRole> businessRoles = businessRoleService.findParents(businessRoleId);
        List<BusinessRoleDTO> businessRoleDTOs = businessRoles.stream().map(BusinessRole::toDTO).toList();
        return ResponseEntity.ok(businessRoleDTOs);
    }

    @Override
    @PermissionCheck(entity = "business-role", action = "update")
    public ResponseEntity<BusinessRoleDTO> updateBusinessRole(
            UUID businessRoleId, @Valid BusinessRolePostDTO businessRoleDTO
    ) {
        businessRoleDTO.setId(businessRoleId);
        BusinessRole businessRole = BusinessRole.fromPostDTO(businessRoleDTO);
        BusinessRole updatedBusinessRole = businessRoleService.update(businessRole);
        return ResponseEntity.ok(updatedBusinessRole.toDTO());
    }

    @Override
    @PermissionCheck(entity = "business-role", action = "update")
    public ResponseEntity<Void> addRoleToBusinessRole(UUID businessRoleId, UUID roleId) {
        businessRoleService.addRole(businessRoleId, roleId);
        return null;
    }

    @Override
    @PermissionCheck(entity = "business-role", action = "update")
    public ResponseEntity<Void> removeRoleFromBusinessRole(UUID businessRoleId, UUID roleId) {
        businessRoleService.removeRole(businessRoleId, roleId);
        return null;
    }

    @Override
    @PermissionCheck(entity = "business-role", action = "get")
    public ResponseEntity<List<RoleDTO>> getRolesByBusinessRoleId(UUID businessRoleId) {
        List<Role> roles = businessRoleService.getRolesByBusinessRoleId(businessRoleId);
        List<RoleDTO> roleDTOs = roles.stream().map(Role::toDTO).toList();
        return ResponseEntity.ok(roleDTOs);
    }
}
