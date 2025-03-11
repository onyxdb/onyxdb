package com.onyxdb.idm.models.clickhouse;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public record BusinessRoleHistory(
        UUID record_id,
        UUID business_role_id,
        String difference,
        LocalDateTime createdAt
) {

    public static BusinessRoleHistory create(UUID business_role_id, String difference) {
        return new BusinessRoleHistory(
                UUID.randomUUID(),
                business_role_id,
                difference,
                LocalDateTime.now()
        );
    }
}
