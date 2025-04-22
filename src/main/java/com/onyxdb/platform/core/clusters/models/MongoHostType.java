package com.onyxdb.platform.core.clusters.models;

import com.onyxdb.platform.utils.StringEnum;
import com.onyxdb.platform.utils.StringEnumResolver;

public enum MongoHostType implements StringEnum {
    UNKNOWN("unknown"),
    MONGOD("mongod");

    public static final StringEnumResolver<MongoHostType> R = new StringEnumResolver<>(MongoHostType.class);

    private final String value;

    MongoHostType(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
