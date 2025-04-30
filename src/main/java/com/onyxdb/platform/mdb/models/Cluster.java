package com.onyxdb.platform.mdb.models;

import java.util.UUID;

/**
 * @author foxleren
 */
// TODO store namespace
// TODO add status
public record Cluster(
        UUID id,
        String name,
        String description,
        UUID projectId,
        ClusterType type,
        ClusterConfig config
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String description;
        private UUID projectId;
        private ClusterType type;
        private ClusterConfig config;


    }
}
