package com.onyxdb.idm.models;

import java.util.List;

import com.onyxdb.idm.generated.openapi.models.RoleWithPermissionsDTO;


/**
 * @author ArtemFed
 */
public record RoleWithPermissions(
        Role role,
        List<Permission> permissions
) {
    public static RoleWithPermissions fromDTO(RoleWithPermissionsDTO roleDTO) {
        return new RoleWithPermissions(
                Role.fromDTO(roleDTO.getRole()),
                roleDTO.getPermissions().stream().map(Permission::fromDTO).toList()
        );
    }

    public RoleWithPermissionsDTO toDTO() {
        return new RoleWithPermissionsDTO()
                .role(role.toDTO())
                .permissions(permissions.stream().map(Permission::toDTO).toList());
    }
}