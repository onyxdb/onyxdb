package com.onyxdb.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.jooq.tables.records.RoleRequestTableRecord;
import com.onyxdb.idm.generated.openapi.models.RoleRequestDTO;

/**
 * @author ArtemFed
 */
public record RoleRequest(
        UUID id,
        UUID roleId,
        UUID accountId,
        UUID ownerId,
        String reason,
        String status,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt
) {
    public RoleRequestDTO toDTO() {
        return new RoleRequestDTO()
                .id(id)
                .roleId(roleId)
                .accountId(accountId)
                .ownerId(ownerId)
                .reason(reason)
                .status(status)
                .createdAt(createdAt)
                .resolvedAt(resolvedAt);
    }

    public static RoleRequest fromDTO(RoleRequestDTO RoleRequestDTO) {
        return new RoleRequest(
                RoleRequestDTO.getId(),
                RoleRequestDTO.getRoleId(),
                RoleRequestDTO.getAccountId(),
                RoleRequestDTO.getOwnerId(),
                RoleRequestDTO.getReason(),
                RoleRequestDTO.getStatus(),
                RoleRequestDTO.getCreatedAt(),
                RoleRequestDTO.getResolvedAt()
        );
    }

    public static RoleRequest fromDAO(RoleRequestTableRecord roleDAO) {
        return new RoleRequest(
                roleDAO.getId(),
                roleDAO.getRoleId(),
                roleDAO.getAccountId(),
                roleDAO.getOwnerId(),
                roleDAO.getReason(),
                roleDAO.getStatus(),
                roleDAO.getCreatedAt(),
                roleDAO.getResolvedAt()
        );
    }
}