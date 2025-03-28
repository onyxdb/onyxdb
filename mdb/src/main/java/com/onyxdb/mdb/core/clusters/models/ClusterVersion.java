package com.onyxdb.mdb.core.clusters.models;

import com.onyxdb.mdb.utils.StringEnum;
import com.onyxdb.mdb.utils.StringEnumResolver;

/**
 * @author foxleren
 */
public enum ClusterVersion implements StringEnum {
    MONGODB_8_0(ClusterType.MONGODB, "8.0"),
    ;

    private static final StringEnumResolver<ClusterVersion> R = new StringEnumResolver<>(ClusterVersion.class);

    private final ClusterType type;
    private final String version;

    ClusterVersion(ClusterType type, String version) {
        this.type = type;
        this.version = version;
    }

    @Override
    public String value() {
        return String.format("%s_%s", type.value(), getVersionWithoutDots(version));
    }

    // TODO throw if version if invalid
    public static ClusterVersion fromStringVersionWithDots(ClusterType type, String version) {
        return R.fromValue(typeAndVersionWithDotsToString(type, version));
    }

    public static String getVersionWithoutDots(String versionWithDots) {
        return versionWithDots.replace(".", "_");
    }

    private static String typeAndVersionWithDotsToString(ClusterType type, String versionWithDots) {
        return String.format("%s_%s", type.value(), getVersionWithoutDots(versionWithDots));
    }
}
