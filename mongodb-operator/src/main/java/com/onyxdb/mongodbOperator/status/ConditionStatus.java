package com.onyxdb.mongodbOperator.status;

/**
 * @author foxleren
 */
public enum ConditionStatus {
    TRUE("True"),
    FALSE("False"),
    UNKNOWN("Unknown"),
    ;

    private final String value;

    ConditionStatus(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
