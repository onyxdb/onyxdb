package com.onyxdb.platform.processing.models;

import java.util.List;
import java.util.UUID;

/**
 * @author foxleren
 */
public record TaskWithBlockers(
        Task task,
        List<UUID> blockerIds
) {
}
