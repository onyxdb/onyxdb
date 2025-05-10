package com.onyxdb.platform.mdb.backups;

import com.onyxdb.platform.mdb.utils.StringEnum;
import com.onyxdb.platform.mdb.utils.StringEnumResolver;


/**
 * @author foxleren
 */
public enum BackupType implements StringEnum {
    AUTOMATED("automated", "Automated"),
    MANUAL("manual", "Manual"),
    ;

    public static final StringEnumResolver<BackupType> R = new StringEnumResolver<>(BackupType.class);

    private final String value;
    private final String displayValue;

    BackupType(final String value, String displayValue) {
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
