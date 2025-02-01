package com.onyxdb.mdb.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author foxleren
 */
public record ClusterTaskWithBlockers(
        ClusterTask task,
        List<UUID> blockerIds
) {
    public static ClusterTaskWithBlockers withoutBlockers(ClusterTask task) {
        return new ClusterTaskWithBlockers(task, new ArrayList<>());
    }
}
