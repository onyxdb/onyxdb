package com.onyxdb.platform.mdb.clusters.models;

import com.onyxdb.platform.mdb.utils.StringEnum;
import com.onyxdb.platform.mdb.utils.StringEnumResolver;


/**
 * @author foxleren
 */
public enum ClusterStatus implements StringEnum {
    UNKNOWN("unknown", "Unknown"),
    ERROR("error", "Error"),
    CREATING("creating", "Creating"),
    READY("ready", "Ready"),
    UPDATING("updating", "Updating"),
    DELETING("deleting", "Deleting"),
    DELETED("deleted", "Deleted"),
    ;

    public static final StringEnumResolver<ClusterStatus> R = new StringEnumResolver<>(ClusterStatus.class);

    private final String value;
    private final String displayValue;

    ClusterStatus(final String value, String displayValue) {
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
