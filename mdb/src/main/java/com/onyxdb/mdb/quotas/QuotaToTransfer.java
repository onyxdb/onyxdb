package com.onyxdb.mdb.quotas;

import java.util.UUID;

public record QuotaToTransfer(
        UUID resourceId,
        long limit
) {
}
