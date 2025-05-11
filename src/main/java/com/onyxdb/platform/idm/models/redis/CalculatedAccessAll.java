package com.onyxdb.platform.idm.models.redis;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onyxdb.platform.idm.models.AccountRolesAll;

/**
 * @author ArtemFed
 */
public record CalculatedAccessAll(
        UUID accountId,
        AccountRolesAll data,
        LocalDateTime expireDate
) {
    public static CalculatedAccessAll create(UUID account_id, AccountRolesAll data, LocalDateTime expireDate) {
        return new CalculatedAccessAll(
                account_id,
                data,
                expireDate
        );
    }
}
