package com.onyxdb.platform.idm.models;

import java.util.List;

import com.onyxdb.platform.generated.openapi.models.BusinessRoleWithRolesDTO;


/**
 * @author ArtemFed
 */
public record BusinessRoleWithRoles(
        BusinessRole businessRole,
        List<RoleWithPermissions> rolesWithPermissions
) {
    public BusinessRoleWithRolesDTO toDTO() {
        return new BusinessRoleWithRolesDTO()
                .businessRole(businessRole.toDTO())
                .roles(rolesWithPermissions.stream().map(RoleWithPermissions::toDTO).toList());
    }
}
