package com.onyxdb.platform.idm.models;

import java.util.List;

import com.onyxdb.platform.generated.openapi.models.RoleWithPermissionsPostDTO;
import com.onyxdb.platform.generated.openapi.models.RoleWithPermissionsDTO;


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

    public static RoleWithPermissions fromPostDTO(RoleWithPermissionsPostDTO roleDTO) {
        return new RoleWithPermissions(
                Role.fromPostDTO(roleDTO.getRole()),
                roleDTO.getPermissions().stream().map(Permission::fromPostDTO).toList()
        );
    }

    public RoleWithPermissionsDTO toDTO() {
        return new RoleWithPermissionsDTO()
                .role(role.toDTO())
                .permissions(permissions.stream().map(Permission::toDTO).toList());
    }
}