package com.onyxdb.mdb.models;

import com.onyxdb.mdb.common.BaseStringEnum;

/**
 * @author foxleren
 */
public enum ClusterOperationStatus implements BaseStringEnum {
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
