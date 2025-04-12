package com.onyxdb.mdb.taskProcessing.models;

import com.onyxdb.mdb.utils.StringEnum;

/**
 * @author foxleren
 */
public enum TaskStatus implements StringEnum {
    SCHEDULED("scheduled"),
    IN_PROGRESS("in_progress"),
    ERROR("error"),
    SUCCESS("success"),
    ;

    private final String value;

    TaskStatus(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    public static TaskStatus fromValue(String value) {
        return StringEnum.fromValue(TaskStatus.class, value);
    }
}
