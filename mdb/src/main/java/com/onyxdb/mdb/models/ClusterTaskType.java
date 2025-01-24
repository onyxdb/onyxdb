package com.onyxdb.mdb.models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author foxleren
 */
public enum ClusterTaskType {
    // MONGODB_CREATE_CLUSTER
    MONGODB_CREATE_CLUSTER_APPLY_CLUSTER_MANIFEST("mongodb_create_cluster_apply_manifest"),
    MONGODB_CREATE_CLUSTER_CHECK_CLUSTER_READINESS("mongodb_create_cluster_check_cluster_readiness"),
    MONGODB_CREATE_CLUSTER_GENERATE_GRAFANA_DASHBOARD("mongodb_create_cluster_generate_grafana_dashboard"),
    ;

    private final String value;

    ClusterTaskType(final String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}
