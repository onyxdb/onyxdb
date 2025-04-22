package com.onyxdb.platform.core.clusters.models;

import com.onyxdb.platform.utils.StringEnum;


/**
 * @author foxleren
 */
public enum ClusterType implements StringEnum {
    MONGODB("mongodb"),
    ;

    private final String value;

    ClusterType(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    // TODO remove
    public static ClusterType fromValue(String value) {
        return StringEnum.fromValue(ClusterType.class, value);
    }
}
