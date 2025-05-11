package com.onyxdb.platform.idm.models.redis;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
public record CalculatedAccessBits(
        UUID accountId,
        Map<String, Optional<Map<String, Object>>> bits,
        LocalDateTime expireDate
) {
    public static CalculatedAccessBits create(UUID account_id, Map<String, Optional<Map<String, Object>>> bits, LocalDateTime expireDate) {
        return new CalculatedAccessBits(
                account_id,
                bits,
                expireDate
        );
    }
}
