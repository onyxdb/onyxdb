package com.onyxdb.mongodbOperator.status;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author foxleren
 */
public enum MongoState {
    ERROR("error"),
    INITIALIZING("initializing"),
    READY("ready"),
    ;

    private final String value;

    MongoState(final String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}
