package com.onyxdb.platform.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.platform.generated.openapi.models.RoleRequestFullDTO;

/**
 * @author ArtemFed
 */
public record RoleRequestFull(
        UUID id,
        Role role,
        Account account,
        Account owner,
        String reason,
        String status,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt
) {
    public RoleRequestFullDTO toDTO() {
        return new RoleRequestFullDTO()
                .id(id)
                .role(role.toDTO())
                .account(account.toDTO())
                .owner(owner.toDTO())
                .reason(reason)
                .status(RoleRequestFullDTO.StatusEnum.valueOf(status))
                .createdAt(createdAt)
                .resolvedAt(resolvedAt);
    }
}