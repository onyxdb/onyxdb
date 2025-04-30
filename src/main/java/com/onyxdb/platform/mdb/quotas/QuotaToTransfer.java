package com.onyxdb.platform.mdb.quotas;

import java.util.UUID;

public record QuotaToTransfer(
        UUID resourceId,
        long limit
) {
}
