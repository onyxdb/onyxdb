package com.onyxdb.mdb.models;

import com.onyxdb.mdb.common.BaseStringEnum;

/**
 * @author foxleren
 */
public enum ClusterTaskStatus implements BaseStringEnum {
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
        return BaseStringEnum.fromValue(ClusterTaskStatus.class, value);
    }
}
