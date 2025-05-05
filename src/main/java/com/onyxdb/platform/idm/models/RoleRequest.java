package com.onyxdb.platform.idm.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.platform.generated.jooq.tables.records.RoleRequestTableRecord;
import com.onyxdb.platform.generated.openapi.models.RoleRequestCreateDTO;
import com.onyxdb.platform.generated.openapi.models.RoleRequestDTO;

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
    public static RoleRequest fromDTO(RoleRequestDTO RoleRequestDTO) {
        return new RoleRequest(
                RoleRequestDTO.getId(),
                RoleRequestDTO.getRoleId(),
                RoleRequestDTO.getAccountId(),
                RoleRequestDTO.getOwnerId(),
                RoleRequestDTO.getReason(),
                RoleRequestDTO.getStatus().toString(),
                RoleRequestDTO.getCreatedAt(),
                RoleRequestDTO.getResolvedAt()
        );
    }

    public static RoleRequest fromCreateDTO(RoleRequestCreateDTO RoleRequestDTO) {
        return new RoleRequest(
                null,
                RoleRequestDTO.getRoleId(),
                RoleRequestDTO.getAccountId(),
                RoleRequestDTO.getOwnerId(),
                RoleRequestDTO.getReason(),
                RoleRequestDTO.getStatus().toString(),
                null,
                null
        );
    }

    public static RoleRequest fromDAO(RoleRequestTableRecord roleRequestDAO) {
        return new RoleRequest(
                roleRequestDAO.getId(),
                roleRequestDAO.getRoleId(),
                roleRequestDAO.getAccountId(),
                roleRequestDAO.getOwnerId(),
                roleRequestDAO.getReason(),
                roleRequestDAO.getStatus(),
                roleRequestDAO.getCreatedAt(),
                roleRequestDAO.getResolvedAt()
        );
    }

    public RoleRequestDTO toDTO() {
        return new RoleRequestDTO()
                .id(id)
                .roleId(roleId)
                .accountId(accountId)
                .ownerId(ownerId)
                .reason(reason)
                .status(RoleRequestDTO.StatusEnum.valueOf(status))
                .createdAt(createdAt)
                .resolvedAt(resolvedAt);
    }
}