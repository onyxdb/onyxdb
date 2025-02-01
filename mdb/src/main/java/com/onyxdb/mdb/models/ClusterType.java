package com.onyxdb.mdb.models;

import com.onyxdb.mdb.common.BaseStringEnum;


/**
 * @author foxleren
 */
public enum ClusterType implements BaseStringEnum {
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

    public static ClusterType fromValue(String value) {
        return BaseStringEnum.fromValue(ClusterType.class, value);
    }
}
