package com.onyxdb.platform.quotas;

import com.onyxdb.platform.utils.StringEnum;
import com.onyxdb.platform.utils.StringEnumResolver;

public enum QuotaProvider implements StringEnum {
    MDB("mdb"),
    ;

    public static final StringEnumResolver<QuotaProvider> R = new StringEnumResolver<>(QuotaProvider.class);

    private final String value;

    QuotaProvider(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
