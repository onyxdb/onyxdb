package com.onyxdb.mongodbOperator.utils;

/**
 * @author foxleren
 */
public enum PodPhase {
    PENDING("Pending"),
    RUNNING("Running"),
    SUCCEEDED("Succeeded"),
    FAILED("Failed"),
    UNKNOWN("Unknown"),
    ;

    private final String value;

    PodPhase(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
