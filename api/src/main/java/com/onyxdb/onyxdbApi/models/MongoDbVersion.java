package com.onyxdb.onyxdbApi.models;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author foxleren
 */
public enum MongoDbVersion {
    V5_0("5.0"),
    V6_0("6.0"),
    ;

    private final String value;

    MongoDbVersion(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Optional<MongoDbVersion> parseO(String value) {
        return Arrays.stream(values())
                .filter(item -> item.toString().equalsIgnoreCase(value))
                .findFirst();
    }
}
