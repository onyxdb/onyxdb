package com.onyxdb.platform.core.clusters.models;

import com.onyxdb.platform.utils.StringEnum;
import com.onyxdb.platform.utils.StringEnumResolver;

public enum MongoRole implements StringEnum {
    READ("read"),
    READ_WRITE("readWrite"),
    ;

    public static final StringEnumResolver<MongoRole> R = new StringEnumResolver<>(MongoRole.class);

    private final String value;

    MongoRole(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
