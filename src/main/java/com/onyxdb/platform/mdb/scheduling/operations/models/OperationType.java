package com.onyxdb.platform.mdb.scheduling.operations.models;

import com.onyxdb.platform.mdb.utils.StringEnum;
import com.onyxdb.platform.mdb.utils.StringEnumResolver;

/**
 * @author foxleren
 */
public enum OperationType implements StringEnum {
    MONGO_CREATE_CLUSTER("mongo_create_cluster", "Create MongoDB cluster"),
    MONGO_CREATE_DATABASE("mongo_create_database", "Create MongoDB database"),
    MONGO_DELETE_DATABASE("mongo_delete_database", "Delete MongoDB database"),
    MONGO_CREATE_USER("mongo_create_user", "Create MongoDB userName"),
    MONGO_DELETE_USER("mongo_delete_user", "Delete MongoDB userName"),
    MONGO_SCALE_CLUSTER("mongo_scale_cluster", "Scale MongoDB cluster"),
    MONGO_DELETE_CLUSTER("mongo_delete_cluster", "Delete MongoDB cluster"),
    MONGO_CREATE_BACKUP("mongo_create_backup", "Create MongoDB backup"),
    ;

    public static final StringEnumResolver<OperationType> R = new StringEnumResolver<>(OperationType.class);

    private final String value;
    private final String displayValue;

    OperationType(String value, String displayValue) {
        this.value = value;
        this.displayValue = displayValue;
    }

    @Override
    public String value() {
        return value;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
