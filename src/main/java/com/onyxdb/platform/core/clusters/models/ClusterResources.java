package com.onyxdb.platform.core.clusters.models;

import java.util.UUID;

/**
 * @author foxleren
 */
public record ClusterResources(
        UUID presetId,
        String storageClass,
        long storage
) {
}
