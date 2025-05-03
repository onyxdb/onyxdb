package com.onyxdb.platform.mdb.clusters.models;

import com.onyxdb.platform.mdb.utils.StringEnum;
import com.onyxdb.platform.mdb.utils.StringEnumResolver;

public enum MongoHostStatus implements StringEnum {
    UNKNOWN("unknown"),
    PRIMARY("alive"),
    SECONDARY("dead"),
    ;

    public static final StringEnumResolver<MongoHostStatus> R = new StringEnumResolver<>(MongoHostStatus.class);

    private final String value;

    MongoHostStatus(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
