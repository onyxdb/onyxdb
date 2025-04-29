package com.onyxdb.platform.idm.models.redis;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public record RefreshToken(
        UUID token,
        UUID accountId,
        LocalDateTime expireDate
) {
    public static RefreshToken create(UUID account_id, UUID token, LocalDateTime expireDate) {
        return new RefreshToken(
                token,
                account_id,
                expireDate
        );
    }
}
