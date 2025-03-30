package com.onyxdb.mdb.core.clusters.models;

import com.onyxdb.mdb.utils.StringEnum;

/**
 * @author foxleren
 */
public enum ClusterOperationType implements StringEnum {
    CREATE_CLUSTER("create_cluster"),
    ;

    private final String value;

    ClusterOperationType(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
