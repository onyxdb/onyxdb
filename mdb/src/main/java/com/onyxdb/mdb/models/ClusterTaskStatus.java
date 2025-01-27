package com.onyxdb.mdb.models;

import com.onyxdb.mdb.utils.BaseStringEnum;

/**
 * @author foxleren
 */
public enum ClusterTaskStatus implements BaseStringEnum {
    SCHEDULED("scheduled"),
    IN_PROGRESS("in_progress"),
    SUCCESS("success"),
    ERROR("error"),
    ;

    private final String value;

    ClusterTaskStatus(final String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
