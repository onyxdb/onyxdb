package com.onyxdb.platform.mdb.clusters.models;

import com.onyxdb.platform.mdb.utils.StringEnum;
import com.onyxdb.platform.mdb.utils.StringEnumResolver;

/**
 * @author foxleren
 */
public enum ClusterVersion implements StringEnum {
    MONGODB_7_0(ClusterType.MONGODB, "7.0"),
    ;

    public static final StringEnumResolver<ClusterVersion> R = new StringEnumResolver<>(ClusterVersion.class);

    private final ClusterType type;
    private final String version;

    ClusterVersion(ClusterType type, String version) {
        this.type = type;
        this.version = version;
    }

    @Override
    public String value() {
        return typeAndVersionAsString(type, version);
    }

    public String getVersion() {
        return version;
    }

    public static String typeAndVersionAsString(ClusterType type, String version) {
        return String.format("%s_%s", type.value(), version);
    }
}
