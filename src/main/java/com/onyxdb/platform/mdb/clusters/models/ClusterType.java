package com.onyxdb.platform.mdb.clusters.models;

import com.onyxdb.platform.mdb.utils.StringEnum;
import com.onyxdb.platform.mdb.utils.StringEnumResolver;


/**
 * @author foxleren
 */
public enum ClusterType implements StringEnum {
    MONGODB("mongodb"),
    ;

    public static final StringEnumResolver<ClusterType> R = new StringEnumResolver<>(ClusterType.class);

    private final String value;

    ClusterType(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
