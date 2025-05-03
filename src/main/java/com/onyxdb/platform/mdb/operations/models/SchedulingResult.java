package com.onyxdb.platform.mdb.operations.models;

import java.time.LocalDateTime;

public record SchedulingResult(
        LocalDateTime scheduledAt,
        int attempts
) {
}
