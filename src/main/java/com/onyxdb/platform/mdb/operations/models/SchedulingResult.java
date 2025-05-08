package com.onyxdb.platform.mdb.operations.models;

public record SchedulingResult(
        int attempts,
        int postDelaySeconds,
        int retryDelaySeconds
) {
}
