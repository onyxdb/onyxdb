package com.onyxdb.platform.mdb.operations.models;

import com.onyxdb.platform.mdb.utils.StringEnum;

/**
 * @author foxleren
 */
public enum TaskStatus implements StringEnum {
    SCHEDULED("scheduled"),
    RESCHEDULED("rescheduled"),
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
