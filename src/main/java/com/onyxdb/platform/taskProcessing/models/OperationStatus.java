package com.onyxdb.platform.taskProcessing.models;

import com.onyxdb.platform.utils.StringEnum;
import com.onyxdb.platform.utils.StringEnumResolver;

/**
 * @author foxleren
 */
public enum OperationStatus implements StringEnum {
    // TODO may be remove scheduled?
    SCHEDULED("scheduled", "Scheduled"),
    IN_PROGRESS("in_progress", "In Progress"),
    ERROR("error", "Error"),
    SUCCESS("success", "Success"),
    ;

    public static final StringEnumResolver<OperationStatus> R = new StringEnumResolver<>(OperationStatus.class);

    private final String value;
    private final String displayValue;

    OperationStatus(String value, String displayValue) {
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
