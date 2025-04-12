package com.onyxdb.mdb.taskProcessing.models;

import java.util.ArrayList;
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
