package com.onyxdb.platform.idm.models.clickhouse;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.platform.generated.openapi.models.RoleHistoryDTO;

/**
 * @author ArtemFed
 */
public record RoleHistory(
        UUID record_id,
        UUID role_id,
        String difference,
        LocalDateTime createdAt
) {

    public static RoleHistory create(UUID role_id, String difference) {
        return new RoleHistory(
                UUID.randomUUID(),
                role_id,
                difference,
                LocalDateTime.now()
        );
    }

    public static RoleHistory fromDTO(RoleHistoryDTO roleDTO) {
        return new RoleHistory(
                roleDTO.getRecordId(),
                roleDTO.getRoleId(),
                roleDTO.getDifference(),
                roleDTO.getCreatedAt()
        );
    }

    public RoleHistoryDTO toDTO() {
        return new RoleHistoryDTO()
                .recordId(record_id)
                .roleId(role_id)
                .difference(difference)
                .createdAt(createdAt);
    }
}
