package com.onyxdb.idm.models;

import java.util.UUID;

import com.onyxdb.idm.generated.openapi.models.AccountResourceRoleDTO;

public record AccountResourceRole(
        UUID accountId,
        UUID resourceId,
        UUID roleId
) {
    public AccountResourceRoleDTO toDTO() {
        return new AccountResourceRoleDTO()
                .accountId(accountId)
                .resourceId(resourceId)
                .roleId(roleId);
    }

    public static AccountResourceRole fromDTO(AccountResourceRoleDTO accountResourceRoleDTO) {
        return new AccountResourceRole(
                accountResourceRoleDTO.getAccountId(),
                accountResourceRoleDTO.getResourceId(),
                accountResourceRoleDTO.getRoleId()
        );
    }
}
