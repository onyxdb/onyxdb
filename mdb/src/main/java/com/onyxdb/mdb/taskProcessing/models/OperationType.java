package com.onyxdb.mdb.taskProcessing.models;

import com.onyxdb.mdb.utils.StringEnum;

/**
 * @author foxleren
 */
public enum OperationType implements StringEnum {
    MONGODB_CREATE_CLUSTER("mongodb_create_cluster"),
    MONGODB_DELETE_CLUSTER("mongodb_delete_cluster"),
    ;

    private final String value;

    OperationType(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
