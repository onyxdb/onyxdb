package com.onyxdb.mdb.models;

import com.onyxdb.mdb.utils.StringEnum;

/**
 * @author foxleren
 */
public enum ClusterTaskStatus implements StringEnum {
    SCHEDULED("scheduled"),
    IN_PROGRESS("in_progress"),
    ERROR("error"),
    SUCCESS("success"),
    ;

    private final String value;

    ClusterTaskStatus(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    public static ClusterTaskStatus fromValue(String value) {
        return StringEnum.fromValue(ClusterTaskStatus.class, value);
    }
}
