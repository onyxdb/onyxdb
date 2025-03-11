package com.onyxdb.idm.models.redis;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public record RefreshToken(
        UUID id,
        UUID accountId,
        UUID token,
        LocalDateTime expireDate
) {
    public static RefreshToken create(UUID account_id, UUID token, LocalDateTime expireDate) {
        return new RefreshToken(
                UUID.randomUUID(),
                account_id,
                token,
                expireDate
        );
    }
}
