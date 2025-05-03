package com.onyxdb.platform.mdb.clusters.models;

import com.onyxdb.platform.mdb.utils.StringEnum;
import com.onyxdb.platform.mdb.utils.StringEnumResolver;

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
