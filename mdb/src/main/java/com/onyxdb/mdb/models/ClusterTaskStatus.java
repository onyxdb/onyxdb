package com.onyxdb.mdb.models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author foxleren
 */
public enum ClusterTaskStatus {
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
    @JsonValue
    public String toString() {
        return value;
    }
}
