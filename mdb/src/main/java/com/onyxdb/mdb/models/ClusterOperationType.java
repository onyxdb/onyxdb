package com.onyxdb.mdb.models;

import com.onyxdb.mdb.common.BaseStringEnum;

/**
 * @author foxleren
 */
public enum ClusterOperationType implements BaseStringEnum {
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
