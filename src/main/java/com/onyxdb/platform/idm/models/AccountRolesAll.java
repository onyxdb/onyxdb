package com.onyxdb.platform.idm.models;

import java.util.List;

import com.onyxdb.platform.generated.openapi.models.AccountRolesAllDTO;
import com.onyxdb.platform.generated.openapi.models.RoleWithPermissionsDTO;
import com.onyxdb.platform.generated.openapi.models.RoleWithPermissionsPostDTO;


/**
 * @author ArtemFed
 */
public record AccountRolesAll(
        List<RoleWithPermissions> roles,
        List<BusinessRoleWithRoles> businessRoles
) {
    public AccountRolesAllDTO toDTO() {
        return new AccountRolesAllDTO()
                .roles(roles.stream().map(RoleWithPermissions::toDTO).toList())
                .businessRoles(businessRoles.stream().map(BusinessRoleWithRoles::toDTO).toList());
    }
}