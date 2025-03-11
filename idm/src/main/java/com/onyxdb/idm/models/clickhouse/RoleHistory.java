package com.onyxdb.idm.models.clickhouse;

import java.time.LocalDateTime;
import java.util.UUID;

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
}
