package com.onyxdb.platform.mdb.scheduling.tasks.models;

import java.time.LocalDateTime;

public record SchedulingResult(
        LocalDateTime scheduledAt,
        int attempts
) {
}
