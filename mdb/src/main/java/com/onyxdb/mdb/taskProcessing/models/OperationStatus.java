package com.onyxdb.mdb.taskProcessing.models;

import com.onyxdb.mdb.utils.StringEnum;

/**
 * @author foxleren
 */
public enum OperationStatus implements StringEnum {
    SCHEDULED("scheduled"),
    IN_PROGRESS("in_progress"),
    ERROR("error"),
    SUCCESS("success"),
    ;

    private final String value;

    OperationStatus(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
