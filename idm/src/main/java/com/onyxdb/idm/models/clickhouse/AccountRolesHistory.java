package com.onyxdb.idm.models.clickhouse;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.openapi.models.AccountRolesHistoryDTO;

/**
 * @author ArtemFed
 */
public record AccountRolesHistory(
        UUID record_id,
        UUID account_id,
        UUID role_id,
        String status,
        LocalDateTime createdAt
) {

    public static AccountRolesHistory create(UUID account_id, UUID role_id, String status) {
        return new AccountRolesHistory(
                UUID.randomUUID(),
                account_id,
                role_id,
                status,
                LocalDateTime.now()
        );
    }

    public AccountRolesHistoryDTO toDTO() {
        return new AccountRolesHistoryDTO()
                .recordId(record_id)
                .accountId(account_id)
                .roleId(role_id)
                .status(status)
                .createdAt(createdAt);
    }
}
