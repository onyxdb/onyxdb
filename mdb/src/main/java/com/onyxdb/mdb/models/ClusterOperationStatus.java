package com.onyxdb.mdb.models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author foxleren
 */
public enum ClusterOperationStatus {
    IN_PROGRESS("in_progress"),
    ERROR("error"),
    SUCCESS("success"),
    ;

    private final String value;

    ClusterOperationStatus(final String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}
