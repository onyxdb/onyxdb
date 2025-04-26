package com.onyxdb.platform.processing.models;

import com.onyxdb.platform.utils.StringEnum;
import com.onyxdb.platform.utils.StringEnumResolver;

/**
 * @author foxleren
 */
public enum OperationStatus implements StringEnum {
    SCHEDULED("scheduled", "Scheduled"),
    IN_PROGRESS("in_progress", "In progress"),
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
