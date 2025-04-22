package com.onyxdb.platform.quotas;

import java.util.UUID;

public record QuotaToTransfer(
        UUID resourceId,
        long limit
) {
}
