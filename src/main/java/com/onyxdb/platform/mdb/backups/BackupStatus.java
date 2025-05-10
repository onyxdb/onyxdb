package com.onyxdb.platform.mdb.backups;

import com.onyxdb.platform.mdb.utils.StringEnum;
import com.onyxdb.platform.mdb.utils.StringEnumResolver;


/**
 * @author foxleren
 */
public enum BackupStatus implements StringEnum {
    UNKNOWN("unknown", "Unknown"),
    ERROR("error", "Error"),
    RUNNING("running", "Running"),
    READY("ready", "Ready"),
    ;

    public static final StringEnumResolver<BackupStatus> R = new StringEnumResolver<>(BackupStatus.class);

    private final String value;
    private final String displayValue;

    BackupStatus(final String value, String displayValue) {
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
