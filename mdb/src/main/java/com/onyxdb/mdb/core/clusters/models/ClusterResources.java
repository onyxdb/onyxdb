package com.onyxdb.mdb.core.clusters.models;

/**
 * @author foxleren
 */
public record ClusterResources(
        String presetId,
        String storageClass,
        long storage
) {
}
