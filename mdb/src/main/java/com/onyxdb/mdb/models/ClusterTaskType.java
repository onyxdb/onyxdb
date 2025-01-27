package com.onyxdb.mdb.models;

import com.onyxdb.mdb.utils.BaseStringEnum;

/**
 * @author foxleren
 */
public enum ClusterTaskType implements BaseStringEnum {
    // MONGODB_CREATE_CLUSTER
    MONGODB_CREATE_CLUSTER_APPLY_MANIFEST("mongodb_create_cluster_apply_manifest"),
    MONGODB_CREATE_CLUSTER_SAVE_HOSTS("mongodb_create_cluster_save_hosts"),
    MONGODB_CREATE_CLUSTER_GENERATE_GRAFANA_DASHBOARD("mongodb_create_cluster_generate_grafana_dashboard"),
    ;

    private final String value;

    ClusterTaskType(final String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
