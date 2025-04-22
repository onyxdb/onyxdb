package com.onyxdb.platform.taskProcessing.models;

import com.onyxdb.platform.utils.StringEnum;

/**
 * @author foxleren
 */
public enum OperationType implements StringEnum {
    MONGODB_CREATE_CLUSTER("mongodb_create_cluster"),
    MONGODB_SCALE_HOSTS("mongodb_scale_hosts"),
    MONGODB_SCALE_CLUSTER("mongodb_scale_cluster"),
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
