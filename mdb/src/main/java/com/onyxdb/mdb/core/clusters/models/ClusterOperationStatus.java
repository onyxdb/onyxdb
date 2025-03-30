package com.onyxdb.mdb.core.clusters.models;

import com.onyxdb.mdb.utils.StringEnum;

/**
 * @author foxleren
 */
public enum ClusterOperationStatus implements StringEnum {
    SCHEDULED("scheduled"),
    IN_PROGRESS("in_progress"),
    ERROR("error"),
    SUCCESS("success"),
    ;

    private final String value;

    ClusterOperationStatus(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
