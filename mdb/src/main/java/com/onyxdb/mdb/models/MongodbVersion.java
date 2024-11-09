package com.onyxdb.mdb.models;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author foxleren
 */
public enum MongodbVersion {
    V5_0("5.0"),
    V6_0("6.0"),
    ;

    private final String value;

    MongodbVersion(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Optional<MongodbVersion> parseO(String value) {
        return Arrays.stream(values())
                .filter(item -> item.toString().equalsIgnoreCase(value))
                .findFirst();
    }
}
