package com.onyxdb.platform.core.clusters.models;

import com.onyxdb.platform.utils.StringEnum;
import com.onyxdb.platform.utils.StringEnumResolver;

public enum MongoHostRole implements StringEnum {
    UNKNOWN("unknown"),
    PRIMARY("primary"),
    SECONDARY("secondary"),
    ;

    public static final StringEnumResolver<MongoHostRole> R = new StringEnumResolver<>(MongoHostRole.class);

    private final String value;

    MongoHostRole(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
