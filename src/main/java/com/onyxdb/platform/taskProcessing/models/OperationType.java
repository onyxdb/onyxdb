package com.onyxdb.platform.taskProcessing.models;

import com.onyxdb.platform.utils.StringEnum;
import com.onyxdb.platform.utils.StringEnumResolver;

/**
 * @author foxleren
 */
public enum OperationType implements StringEnum {
    MONGODB_CREATE_CLUSTER("mongodb_create_cluster", "Create MongoDB cluster"),
    MONGODB_SCALE_HOSTS("mongodb_scale_hosts", "Scale MongoDB hosts"),
    MONGODB_SCALE_CLUSTER("mongodb_scale_cluster", "Scale MongoDB cluster"),
    MONGODB_DELETE_CLUSTER("mongodb_delete_cluster", "Delete MongoDB cluster"),
    ;

    public static final StringEnumResolver<OperationType> R = new StringEnumResolver<>(OperationType.class);

    private final String value;
    private final String displayValue;

    OperationType(String value, String displayValue) {
        this.value = value;
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    @Override
    public String value() {
        return value;
    }
}
