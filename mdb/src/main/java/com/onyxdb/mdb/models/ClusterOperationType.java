package com.onyxdb.mdb.models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author foxleren
 */
public enum ClusterOperationType {
    MONGODB_CREATE_CLUSTER("mongodb_create_cluster"),
    MONGODB_DELETE_CLUSTER("mongodb_delete_cluster"),
    ;

    private final String value;

    ClusterOperationType(final String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}
