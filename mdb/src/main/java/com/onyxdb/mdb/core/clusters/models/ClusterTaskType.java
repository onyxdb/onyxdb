package com.onyxdb.mdb.core.clusters.models;

import com.onyxdb.mdb.utils.StringEnum;

/**
 * @author foxleren
 */
public enum ClusterTaskType implements StringEnum {
    APPLY_MANIFEST("apply_manifest"),
    CHECK_READINESS("check_readiness"),
    ;

    private final String value;

    ClusterTaskType(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    public static ClusterTaskType fromValue(String value) {
        return StringEnum.fromValue(ClusterTaskType.class, value);
    }
}
