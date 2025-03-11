package com.onyxdb.idm.models.clickhouse;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.idm.generated.openapi.models.AccountBusinessRolesHistoryDTO;

/**
 * @author ArtemFed
 */
public record AccountBusinessRolesHistory(
        UUID record_id,
        UUID account_id,
        UUID business_role_id,
        String status,
        LocalDateTime createdAt
) {

    public static AccountBusinessRolesHistory create(UUID account_id, UUID business_role_id, String status) {
        return new AccountBusinessRolesHistory(
                UUID.randomUUID(),
                account_id,
                business_role_id,
                status,
                LocalDateTime.now()
        );
    }

    public AccountBusinessRolesHistoryDTO toDTO() {
        return new AccountBusinessRolesHistoryDTO()
                .recordId(record_id)
                .accountId(account_id)
                .businessRoleId(business_role_id)
                .status(status)
                .createdAt(createdAt);
    }
}
