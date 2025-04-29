package com.onyxdb.platform.idm.models.redis;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public record RefreshTokenSer(
        UUID token,
        UUID accountId,
        LocalDateTime expireDate
) {
    public static RefreshTokenSer create(UUID account_id, UUID token, LocalDateTime expireDate) {
        return new RefreshTokenSer(
                token,
                account_id,
                expireDate
        );
    }
}
