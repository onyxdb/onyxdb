package com.onyxdb.idm.controllers.v1;

import com.onyxdb.idm.generated.openapi.apis.BusinessRolesApi;
import com.onyxdb.idm.generated.openapi.models.BusinessRoleDTO;
import com.onyxdb.idm.generated.openapi.models.RoleDTO;
import com.onyxdb.idm.models.BusinessRole;
import com.onyxdb.idm.models.Role;
import com.onyxdb.idm.services.BusinessRoleService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BusinessRolesController implements BusinessRolesApi {
    private final BusinessRoleService businessRoleService;

    @Override
    public ResponseEntity<BusinessRoleDTO> createBusinessRole(@Valid BusinessRoleDTO businessRoleDTO) {
        BusinessRole businessRole = BusinessRole.fromDTO(businessRoleDTO);
        BusinessRole createdBusinessRole = businessRoleService.create(businessRole);
        return new ResponseEntity<>(createdBusinessRole.toDTO(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteBusinessRole(UUID businessRoleId) {
        businessRoleService.delete(businessRoleId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<BusinessRoleDTO> getBusinessRoleById(UUID businessRoleId) {
        BusinessRole businessRole = businessRoleService.findById(businessRoleId);
        return ResponseEntity.ok(businessRole.toDTO());
    }

    @Override
    public ResponseEntity<List<BusinessRoleDTO>> getAllBusinessRoles() {
        List<BusinessRole> businessRoles = businessRoleService.findAll();
        List<BusinessRoleDTO> businessRoleDTOs = businessRoles.stream().map(BusinessRole::toDTO).toList();
        return ResponseEntity.ok(businessRoleDTOs);
    }

    @Override
    public ResponseEntity<BusinessRoleDTO> updateBusinessRole(
            UUID businessRoleId, @Valid BusinessRoleDTO businessRoleDTO
    ) {
        businessRoleDTO.setId(businessRoleId);
        BusinessRole businessRole = BusinessRole.fromDTO(businessRoleDTO);
        BusinessRole updatedBusinessRole = businessRoleService.update(businessRole);
        return ResponseEntity.ok(updatedBusinessRole.toDTO());
    }

    @Override
    public ResponseEntity<Void> addRoleToBusinessRole(UUID businessRoleId, UUID roleId) {
        businessRoleService.addRole(businessRoleId, roleId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> removeRoleFromBusinessRole(UUID businessRoleId, UUID roleId) {
        businessRoleService.removeRole(businessRoleId, roleId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<RoleDTO>> getRolesByBusinessRoleId(UUID businessRoleId) {
        List<Role> roles = businessRoleService.getRolesByBusinessRoleId(businessRoleId);
        List<RoleDTO> roleDTOs = roles.stream().map(Role::toDTO).toList();
        return ResponseEntity.ok(roleDTOs);
    }
}
